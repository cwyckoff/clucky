(ns clucky.whitelist
  (:require [clojure.string :refer [join split-lines]]))

(def whitelist (atom #{"localhost.page.connectEnd"
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
                       "localhost.page.unloadEventStart"}))

(defn statsd-key
  "given a statsd packet, return its key"
  ([packet] (statsd-key packet 1))
  ([packet i]
   (if (= (subs packet i (inc i)) \:)
     (subs packet p 0 i)
     (recur packet (inc i)))))

(defn scrub
  "given one or more packets separated by newlines, filter out any that don't
   match any entries in the whitelist"
  [req]
  (join "\n" (filter #(contains? @whitelist (statsd-key %)) (split-lines req))))
