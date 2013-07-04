(ns blueshift.interfaces)

(defmulti find-in-project (fn [x] x))

(defmulti bootstrap! (fn [x] x))

(defmulti shell-run-action (fn [type-name] (keyword type-name)))

(defprotocol ShellRunnableActionProtocol
  "Actions that can be invoked from the command line."
  (documentation [self] "Returns the documentation about the action so it can be printed in the command line.")
  (arguments [self] "Returns a description of the arguments supported by this action.")
  (execute [self args] "Triggers the action receiving as parameters the parsed command line arguments."))

(def ^:dynamic *actions* {})

(defn register-action
  "Registers a new action as available"
  [action-name type]
  (alter-var-root #'*actions* (constantly (assoc *actions* action-name type))))
