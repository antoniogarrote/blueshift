require 'aws-sdk-core'
require 'yaml'

AWS_CREDENTIALS = YAML.load_file(File.join(File.dirname(__FILE__),'config/aws_credentials.yml'))

Aws.config = AWS_CREDENTIALS
