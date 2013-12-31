require 'aws-sdk-core'

require 'yaml'

AWS_CREDENTIALS = YAML.load_file(File.join(File.dirname(__FILE__),'..','config','aws_credentials.yml'))

Aws.config = AWS_CREDENTIALS


require File.join(File.dirname(__FILE__),'blueshift','context')
require File.join(File.dirname(__FILE__),'blueshift','job_flow')
require File.join(File.dirname(__FILE__),'blueshift','steps')
require File.join(File.dirname(__FILE__),'blueshift','steps','s3_copy_step')
