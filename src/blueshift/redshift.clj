(ns blueshift.redshift
  (:use [blueshift interfaces configuration])
  (:import [java.sql Connection DriverManager ResultSet SQLException Statement ResultSetMetaData]
           [org.postgresql Driver]))

(def ^:dynamic *connection* nil)

(defn connect! []
  (let [config (find-in-project [:configuration :redshift])
        host (:hostname config)
        port (:port config)
        database (:database config)
        user (:username config)
        password (:password config)
        url (str "jdbc:postgresql://" host ":" port "/" database)]
    (println (str "URL:" url))
    (alter-var-root #'*connection* (constantly (DriverManager/getConnection url user password)))))
