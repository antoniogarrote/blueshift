(ns blueshift.core)

(defn current-dir []
  (System/getProperty  "user.dir"))

(defmulti find-in-project (fn [x] x))

(defmethod find-in-project :test [what] 
  (println (str "YOU GOT TO THE TEST WITH VALUE " what)))

(defmethod find-in-project [:configuration :redshift] [what]
  (println (str "HEY I'm looking for the configuration")))