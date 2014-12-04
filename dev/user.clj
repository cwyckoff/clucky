(ns user
  (:require [clojure.pprint :refer [pprint]]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [clojure.test :as test]
            [environ.core :refer [env]]
            [com.stuartsierra.component :as component]
            [clucky.core :refer [app]]))

(def system nil)

(defn init []
  (alter-var-root #'system (constantly (app))))

(defn start []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system component/stop))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (refresh :after 'user/go))
