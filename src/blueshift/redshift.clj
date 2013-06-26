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


(defmacro with-connection [& args]
  `(do
     (when (or (nil? ~'*connection*) 
               (.isClosed ~'*connection*)) 
       (connect!))
     ~@args
     (.close ~'*connection*)))


(defn execute [query]
  (with-connection
    (let [stmt (.createStatement *connection*)]
      (.execute stmt query))))


(defn- credentials-string []
  (let [aws (find-in-project [:configuration :aws])]
    (str "aws_access_key_id=" (:access-id aws) 
         ";aws_secret_access_key=" (:private-key aws))))

(defn unload-query-to [query destination]
  (let [unload-query-string (str "UNLOAD ('" (.replace query "'" "\\'") "') "
                                 "TO '" destination "' "
                                 "CREDENTIALS '" (credentials-string) "'")]
    (println (str "QUERY >>" unload-query-string "<<"))
    (execute unload-query-string)))