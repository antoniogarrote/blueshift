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

(defn process-sql-options [options]
  (reduce (fn [ac option] 
            (condp = (keyword option)
              :allow-overwrite  (if (:allow-overwrite options) (str ac " ALLOWOVERWRITE") ac)
              :delimiter  (str ac " DELIMITER AS '" (:delimiter options) "'")
              :gzip       (if (:gzip options) (str ac " GZIP") ac)
              :add-quotes (if (:add-quotes options) (str ac " ADDQUOTES") ac)
              :null       (str ac " NULL AS '" (:null options) "'")
              :escape     (if (:escape options) (str ac " ESCAPE") ac)
              :fixed-width (str ac " FIXEDWIDTH AS '" (:fixed-width options) "'")
              ac))
          ""
          (keys options)))

(defn unload-query-to [query destination & options]
  (let [options (or (first options) {})
        unload-query-string (str "UNLOAD ('" (.replace query "'" "\\'") "') "
                                 "TO '" destination "' "
                                 "CREDENTIALS '" (credentials-string) "'"
                                  (process-sql-options options))]
    (println (str "QUERY >>" unload-query-string "<<"))
    (execute unload-query-string)))