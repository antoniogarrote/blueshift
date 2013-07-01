(ns blueshift.interfaces)

(defmulti find-in-project (fn [x] x))

(defmulti bootstrap! (fn [x] x))

(defmulti shell-run-action (fn [namespace protocol args] [namespace protocol]))

(defprotocol ShellRunnableActionProtocol
  "Actions that can be invoked from the command line."
  (documentation [self] "Returns the documentation about the action so it can be printed in the command line.")
  (parse-args [self args] "Parses command line arguments to build the required input parameters for the action.")
  (execute [self args] "Triggers the action receiving as parameters the parsed command line arguments."))