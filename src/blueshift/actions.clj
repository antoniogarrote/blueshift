(ns blueshift.actions
  (:use [blueshift interfaces])
  (:require [blueshift.redshift :as redshift]))

(deftype SelectToTSV [data] 
  ShellRunnableActionProtocol
  (documentation [self] "Transforms a SELECT query into a TSV file at the specified location. \n Arguments: \n\t-q SELECT query.\n\t-l S3 location path.")
  (arguments [self] [{ :short-name "q" :has-arg true :required true :description "SELECT query that is going to be transformed into a TSV file." :type String },
                     { :short-name "d" :has-arg true :required true :description "S3 path that will be the destination of the query returned data." :type String }])
  (execute [self args] (let [query (:q args)
                             destination (:d args)]
                         (redshift/unload-query-to query destination {:delimiter "\t"}))))

(defmethod shell-run-action :select-to-tsv [type-name]
  (SelectToTSV. type-name))


;; Regiscters the actions
(defmethod bootstrap! :actions [what]
  (register-action :select-to-tsv SelectToTSV))