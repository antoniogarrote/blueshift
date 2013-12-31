require File.join(File.dirname(__FILE__), 'helper')

include Blueshift

describe "S3CopyStep" do
  
  it "should create an object, register it in the context and make its properties available" do
    dst = 's3://bucket/path_in/bucket'
    step = S3CopyStep.new(name: 'mycopystep', src: 'file://local/something', dst: dst)
    step2 = S3CopyStep.new(name: 'mycopystep2', src: 'file://local/something', dst: dst+"2")

    step.output.should be_eql(dst)
    step2.output.should be_eql(dst+"2")
    step.normalize_argument("blueshift://steps/mycopystep/output").should be_eql(dst)
    step.normalize_argument("blueshift://steps/mycopystep2/output").should be_eql(dst+"2")
  end

  it "should be possible to copy data from and to the S3 file system" do
    timestamp = Time.now.to_i.to_s
    tempfile = Tempfile.new("test#{timestamp}.txt")
    path = tempfile.path
    path2 = Tempfile.new("test#{timestamp}_2.txt").path

    tempfile << timestamp
    tempfile.flush

    s3 = Aws::S3.new

    s3.create_bucket(:bucket => 'blueshift_tests') rescue nil # just in case it hasn't been created

    dst = "s3://blueshift_tests#{path}"
    S3CopyStep.new(name: "test_#{timestamp}", src: "file:/#{path}", dst: dst).execute!
    S3CopyStep.new(name: "test_#{timestamp}", src: dst, dst: "file:/#{path2}").execute!    

    read = File.open(path2, "r").read
    
    read.should be_eql(timestamp)
  end
end
