(ns blueshift.redshift
  (:import [java.sql Connection DriverManager ResultSet SQLException Statement ResultSetMetaData]
           [org.postgresql Driver]))

(def ^:dynamic *connection* nil)

(defn connect! [host port database user password]
  (let [url (str "jdbc:postgresql://" host ":" port "/" database)]
    (alter-var-root #'*connection* (constantly (DriverManager/getConnection url user password)))))

