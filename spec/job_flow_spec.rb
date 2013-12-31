require File.join(File.dirname(__FILE__), 'helper')

include Blueshift

describe "JobFlow" do 

  it "should handle config options with default values" do 

    jobflow = JobFlow.new

    jobflow.cluster_name.should_not be_nil
    jobflow.master_instance_type.should be_eql('m1.small')
    jobflow.slave_instance_type.should be_eql('m1.small')
    jobflow.instance_count.should be_eql(1)
    jobflow.id.should be_nil

  end

end

