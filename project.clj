(defproject blueshift "0.0.1-SNAPSHOT"
  :description "Redshift > Mahout > Redshift"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.5.1"]
                 [org.apache.mahout/mahout-core "0.7" :exclusions [org.apache.hadoop/hadoop-core]]
                 [clojure-hadoop/clojure-hadoop "1.4.2"]
                 [clj-aws-s3 "0.3.3"]
                 [org.clojure/data.json "0.2.0"]
                 [postgres-redshift/postgres-redshift "8.4-703.jdbc4"]
                 [commons-cli/commons-cli "1.2"]
                 ])