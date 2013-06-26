(ns blueshift.configuration
  (:use [blueshift interfaces utils])
  (:require [clojure.data.json :as json]))


(def ^:dynamic *configuration* {})

(defmethod bootstrap! :configuration [what]
  (let [data (slurp (str (current-dir) "/config/database.json"))
        database (json/read-str data)
        new-config (assoc-map database *configuration*)]
    (alter-var-root #'*configuration* (constantly new-config))))

(defmethod find-in-project [:configuration :redshift] [what]
  {:hostname (:hostname *configuration*)
   :port (:port *configuration*)
   :database (:database *configuration*)
   :username (:username *configuration*)
   :password (:password *configuration*)})