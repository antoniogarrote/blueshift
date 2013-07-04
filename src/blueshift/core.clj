(ns blueshift.core
  (:use [blueshift interfaces configuration redshift actions utils]
        [clojure.java shell])
  (:gen-class))

; Initialize subsystems

(bootstrap! :configuration)
(bootstrap! :actions)


; Parsing and execution of command line

(defn- print-help-for-action
  "Prints the action assciated documentation"
  [action-name]
  (if (nil? action-name)
    (do (println (str "Usage: blueshift help ACTION_NAME\nAvailable actions:" ))
        (loop [names (keys blueshift.interfaces/*actions*)]
          (when (not (empty? names))
            (println (str "- " (name (first names))))
            (recur (rest names)))))
    (let [action (shell-run-action action-name)]
      (println (str "Help for action: " action-name "\n" (documentation action))))))

(defn run-action
  "Executes the right action for the command line arguments passed in"
  [type-name & args]
  (if (= type-name "help")
    (print-help-for-action (first args))
    (let [action (shell-run-action type-name)
          args (parse-command-line-args args (arguments action))]
      (execute action args))))

(defn shell-out-action
  "Starts a subprocess to run the action from the command line using the current blueshift jar."
  [action args]
  (let [cmd (loop [params (keys args)
                   acum ["./bin/blueshift" (name action)]]
              (if (empty? params) acum
                  (recur (rest params)
                         (-> acum 
                             (conj (str "-" (name (first params))))
                             (conj (str "" (get args (first params)) ""))))))]
    (println (str "shelling out " cmd))
    (apply sh (flatten (list cmd :dir (current-dir))))))

(defn -main [& args] 
  (apply run-action args))