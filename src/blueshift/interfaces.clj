(ns blueshift.interfaces)

(defmulti find-in-project (fn [x] x))

(defmulti bootstrap! (fn [x] x))