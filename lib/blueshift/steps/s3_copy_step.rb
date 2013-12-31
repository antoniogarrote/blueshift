require File.join(File.dirname(__FILE__),'..','steps')

module Blueshift
  class S3CopyStep < Step

    attr_reader :src, :dst

    OPTIONS = [:src, :dst]

    def initialize(options = {})
      super(options[:name])
      OPTIONS.each do |option| 
        value = normalize_argument(options[option])
        raise Exception.new("Missing option #{option} in step  #{name}") if value.nil?
        instance_variable_set :"@#{option}", value
      end
      @input = @src
      @output = @dst
    end

    def execute!
      src_path = parse_file_uri(@src)
      dst_path = parse_file_uri(@dst)
      
      if(src_path[:filesystem] == :s3 && dst_path[:filesystem] == :s3)
        Aws::S3.new.copy_object(:bucket => dst_path[:bucket], :key  => dst_path[:key], :copy_source => @src)
      elsif(src_path[:filesystem] == :file && dst_path[:filesystem] == :s3)
        data = File.open(src_path[:path], "rb")
        Aws::S3.new.put_object(:bucket => dst_path[:bucket], :key  => dst_path[:key], :body => data)
      elsif(src_path[:filesystem] == :s3 && dst_path[:filesystem] == :file)
        resp = Aws::S3.new.get_object(:bucket => src_path[:bucket], :key  => src_path[:key])
        File.open(dst_path[:path], "wb") do |f| 
          resp.body.rewind
          f.write resp.body.read
        end
      else
        raise Exception.new("Raise exception, local copy is not supported")
      end
    end

    def parse_file_uri(uri)
      
      location, path  = if(uri =~ /^file:\/\//)
                          [:file,uri.split(":/").last]
                        else
                          [:s3,uri.split(":/").last]
                        end

      if(location == :file)
        {:filesystem => location, :path => path}
      else
        parts = path.split("/")
        bucket = parts.shift
        key = parts.join("/")
        {:filesystem => location, :bucket => bucket, :key => key}
      end
    end
    
  end
end
