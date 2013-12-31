module Blueshift

  class Context
    
    # Registry of steps
    STEPS = {}

    class << self

      def register_step(step)
        Context::STEPS[step.name] = step
      end

      def steps
        STEPS
      end

      def clear
        STEPS.clear
      end

    end # end of metaclass

  end # end of Context

end # end of Blueshift
