(ns clucky.whitelist
  (:require [clojure.string :refer [join split-lines]]
            [clucky.protocols :as p]
            [clucky.store.redis :as store]))

(def s (store/create))

(defn statsd-key
  "given a statsd packet, return its key"
  ([packet] (statsd-key packet 1))
  ([packet i]
   (if (= (subs packet i (inc i)) ":")
     (subs packet 0 i)
     (recur packet (inc i)))))

(defn is-allowed?
  "given a packet, determine if it's been whitelisted"
  [packet]
  (p/has? s (statsd-key packet) "clucky:whitelist"))

(defn scrub
  "given one or more packets separated by newlines, filter out and log any that
   don't match any entries in the whitelist"
  [req]
  (let [all (group-by is-allowed? (split-lines req))
        good (all true)
        bad (all false)]
    (dorun (for [x bad] (p/add! s (statsd-key x) "clucky:blacklist")))
    (join "\n" good)))
