(ns blueshift.utils)

(defn current-dir []
  (System/getProperty  "user.dir"))

(defn assoc-map [from to]
  (reduce (fn [ac k] (assoc ac (keyword k) (get from k)))
          to
          (keys from)))
