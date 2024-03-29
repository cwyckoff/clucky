(ns clucky.udp
  (:require [environ.core :refer [env]])
  (:import [java.net InetSocketAddress]
           [java.nio ByteBuffer]
           [java.nio.channels DatagramChannel]))

(defn addr
  "Create a new recipient address object"
  []
  (InetSocketAddress. (env :statsd-host) 8125))

(defn chan
  "Create a new Datagram channel connected to the configured address"
  []
  (doto (DatagramChannel/open) (.connect (addr))))

(defn write
  "Write the given message to the recipient"
  [m]
  (.write (chan) (ByteBuffer/wrap m)))
