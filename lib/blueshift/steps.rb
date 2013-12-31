module Blueshift

  class Step
    
    attr_reader :name, :output, :input
      
    def initialize(name)
      @name = name
      Blueshift::Context.register_step(self)
    end

    def run!
      raise Exception.new("Step class has not implemented the run! method")
    end

    def normalize_argument(value)
      if(value =~ /^blueshift:\/\//)
        parts = value.split("blueshift://").last.split("/")
        parts.inject(Blueshift::Context) do |context, part| 
          if context.respond_to?(part)
            context.send(part) 
          else
            context[part] || context[part.to_sym]
          end
        end
      else
        value
      end
    end

  end 

end
