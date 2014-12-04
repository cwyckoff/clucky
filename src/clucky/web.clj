(ns clucky.web
  (:require [org.httpkit.server :refer [run-server]]
            [compojure.core :refer [context GET POST routes]]
            [compojure.handler :refer [api]]
            [compojure.route :refer [not-found]]
            [com.stuartsierra.component :as component]
            [clucky.udp :as udp]
            [clucky.whitelist :as whitelist]))

(def cors-headers
  "CORS headers and values to be appended to requests"
  {"Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Methods" "POST"
   "Access-Control-Max-Age" "604800"
   "Access-Control-Allow-Credentials" "true"})

(defn bytes->string
  "translate a bytesinputbuffer to a string"
  [b]
  (-> b .bytes String.))

(defn add-cors
  "middleware to add CORS headers to response"
  [handler]
  (fn [req]
    (let [res (handler req)
          head (:headers res)]
      (merge-with merge res {:headers cors-headers}))))

(defn app-routes
  "create compojure routes for the application"
  []
  (routes
    (GET "/v1/health-check" [] "OK")
    (POST "/v1/send" [:as {body :body}]
      (let [msg (whitelist/scrub (bytes->string body))]
        (if (> (count msg) 0) (udp/write (.getBytes msg))))
      {:status 204 :headers {} :body ""})
    (not-found "404")))

(defrecord WebServer [handle]
  component/Lifecycle
  (start [this]
    (println ";; starting webserver")
    (let [routes (-> (app-routes) api add-cors)
          handle (run-server routes {:port 3000})]
      (assoc this :handle handle)))
  (stop [this]
    (println ";; stopping webserver")
    (let [handle (:handle this)]
      (handle)
      (assoc this :handle nil))))

(defn create
  "create a new webserver service"
  []
  (map->WebServer {}))
