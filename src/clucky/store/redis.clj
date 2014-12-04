(ns clucky.store.redis
  (:require [clucky.protocols :as p]
            [taoensso.carmine :as car :refer [wcar]]))

(def conn {:pool {} :spec {:host "dinghy.local" :port 6379}})

(defrecord RedisStore [conn]
  p/Store
  (has? [_ k b] (= 1 (wcar conn (car/sismember b k))))
  (add! [_ k b] (wcar conn (car/sadd b k)))
  (remove! [_ k b] (wcar conn (car/srem b k))))

(defn create
  "create a new redis-backed whitelist store"
  []
  (map->RedisStore {:conn conn}))
