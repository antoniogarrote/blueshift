(ns blueshift.core
  (:use [blueshift interfaces configuration redshift]))

; Initialize subsystems
(bootstrap! :configuration)