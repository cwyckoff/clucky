(ns clucky.web
  (:require [org.httpkit.server :refer [run-server]]
            [compojure.core :refer [context GET POST routes]]
            [compojure.handler :refer [api]]
            [compojure.route :refer [not-found]]
            [com.stuartsierra.component :as component]))

(defn app-routes []
  "create compojure routes for the application"
  (routes
    (GET "/health-check" [] "OK")
    (not-found "404")))

(defrecord WebServer [handle]
  component/Lifecycle
  (start [this]
    (println ";; starting webserver")
    (let [routes (api (app-routes))
          handle (run-server routes {:port 3000})]
      (assoc this :handle handle)))
  (stop [this]
    (println ";; stopping webserver")
    (let [handle (:handle this)]
      (handle)
      (assoc this :handle nil))))

(defn create []
  "create a new webserver service"
  (map->WebServer {}))
