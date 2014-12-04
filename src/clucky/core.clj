(ns clucky.core
  (:require [com.stuartsierra.component :as component]
            [clucky.web :as web])
  (:gen-class))

(defn app
  "create a new application system"
  []
  (component/system-map
    :web (web/create)))

(defn -main
  "Start the application"
  [& args]
  (component/start (app)))
