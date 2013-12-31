require 'securerandom'
require 'date'

module Blueshift

  class JobFlow

    attr_reader :cluster_name, :id, :keep_alive
    attr_reader :master_instance_type, :instance_count, :slave_instance_type

    attr_accessor :steps

    OPTIONS = [:master_instance_type, :instance_count, :slave_instance_type]
    DEFAULTS = {
      :master_instance_type => 'm1.small',
      :slave_instance_type  => 'm1.small',
      :instance_count       => 1,
      :keep_alive => false,
    }

    def initialize(config = {})
      @cluster_name = config[:cluster_name] || gen_unique_cluster_name     
      OPTIONS.each do |option|
        instance_variable_set "@#{option}", (config[option] || DEFAULTS[option])
      end
      @steps = []
    end

    def run!
      @id = Aws::EMR.new.run_job_flow(to_aws_hash).job_flow_id
    end

    def terminate!
      with_running_cluster do
        Aws::EMR.new.terminate_job_flows(:job_flow_ids => [@id])
      end
    end

    def status
      with_running_cluster do
        cluster = Aws::EMR.new.list_clusters.clusters.detect do |cluster|
          cluster.name == @cluster_name
        end
        if cluster.nil?
          raise Exception.new("cluster not found #{cluster.name}")
        else
          cluster.status
        end
      end
    end
    
    protected
    
    def gen_unique_cluster_name
      timestamp = Date.today.strftime("%Y_%M_%H_%s")
      "cluster_id_#{SecureRandom.uuid}_#{timestamp}"
    end

    def to_aws_hash
      {
        :name => cluster_name, 
        :instances => {
          :master_instance_type => master_instance_type,
          :slave_instance_type => slave_instance_type,
          :instance_count => instance_count,
        },
        :keep_job_flow_alive_when_no_steps => keep_alive,
        :steps => steps.map(&:to_aws_hash) # steps.map {|s| s.to_aws_hash}
      }
    end

    def raise_not_running
      raise Exception.new("cluster is not running!")
    end
    
    def with_running_cluster
      raise_not_running if @id.nil?
      yield if block_given?
    end

  end


end

