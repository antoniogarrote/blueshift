(ns blueshift.utils
  (:import [org.apache.commons.cli Options Option PosixParser]))

(defn current-dir 
  "Returns the current directory for the running JVM process"
  []
  (System/getProperty  "user.dir"))

(defn assoc-map 
  "Merges keys from hash-map 'from' into hash-map 'to' transforming keys into keywords"
  [from to]
  (reduce (fn [ac k] (assoc ac (keyword k) (get from k)))
          to
          (keys from)))

(defn options-definition 
  "Builds an Apache CLI Options object from an array of arguments with maps {:short-name :has-arg :required :description :type} describing the option. 
   All keys are mandatory."
  [options]
  (loop [remaining options
         parsed (Options.)]
    (if (empty? remaining)
      parsed
      (let [{:keys [short-name has-arg required description type]}  (first remaining)
            option (Option. (name short-name) has-arg description)]
        (.setType option type)
        (.setRequired option required)
        (.addOption parsed option)
        (recur (rest remaining)
               parsed)))))

(defn- parse-command-line-value
  "Tries to coerce the string witht the value of an option into the provided type."
  [value type]
  (condp = type
    Integer (Integer/parseInt value)
    Float (Float/parseFloat value)
    String (str value)
    true value))

(defn parse-command-line-args
  "Parses a vector of command line arguments using the provided options definition."
  [args cmd-line-options]
  (println (str "ARGS " args))
  (println (str "CMD LINE OPTIONS " cmd-line-options))
  (let [options (options-definition cmd-line-options)
        parser (PosixParser.)
        cmd-line (.parse parser options (into-array args))]
    (loop [opts (iterator-seq (.iterator cmd-line))
           acum {}]
      (if (empty? opts)
        acum
        (let [option (first opts)
              opt-name (.getOpt option)
              opt-type (.getType option)
              value (.getOptionValue cmd-line opt-name)]
          (recur (rest opts)
                 (assoc acum (keyword opt-name) (parse-command-line-value value opt-type ))))))))