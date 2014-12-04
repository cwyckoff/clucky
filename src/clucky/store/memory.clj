(ns clucky.store.memory
  (:require [clucky.protocols :as p]))

(def db (atom {:found #{"localhost.page.connectEnd"
                       "localhost.page.connectStart"
                       "localhost.page.domComplete"
                       "localhost.page.domContentLoadedEventEnd"
                       "localhost.page.domContentLoadedEventStart"
                       "localhost.page.domInteractive"
                       "localhost.page.domLoading"
                       "localhost.page.domainLookupEnd"
                       "localhost.page.domainLookupStart"
                       "localhost.page.fetchStart"
                       "localhost.page.loadEventEnd"
                       "localhost.page.loadEventStart"
                       "localhost.page.navigationStart"
                       "localhost.page.responseEnd"
                       "localhost.page.responseStart"
                       "localhost.page.unloadEventEnd"
                       "localhost.page.unloadEventStart"}
               :not-found #{}}))

(defrecord MemoryStore [db not-found]
  p/Store
  (has? [this k b] (contains? (b @(:db this)) k))
  (add! [this k b] (swap! (:db this) update-in [b] conj k))
  (remove! [this k b] (swap! (:db this) update-in [b] disj k)))

(defn create
  "create a new in-memory whitelist store"
  []
  (map->MemoryStore {:db db}))
