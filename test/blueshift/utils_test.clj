(ns blueshift.utils-test
  (:require [clojure.test :refer :all]
            [blueshift.utils :refer :all]))

(deftest options-definition-test1
  (testing "Should be able to build an Options object with the provided options."
    (let [options (options-definition 
                   [{:short-name "c" :has-arg true :required true :description "test option C" :type String} 
                    {:short-name "d" :has-arg false :required true :description "test option D" :type Integer}])]
      (is (.hasOption options "c"))
      (is (.hasOption options "d"))
      (is (not (.hasOption options "e")))
      (let [option (.getOption options "c")]
        (is (= "c" (.getOpt option)))
        (is (= String (.getType option)))
        (is (.isRequired option))))))

(deftest parse-command-line-args-test1
  (testing "Should be able to parse a vector of command line arguments with the provided options descriptiojn."
    (let [result (parse-command-line-args ["-c" "12"] [{:short-name "c" :required false :has-arg true :type Integer}])]
      (is (= 12 (:c result))))))