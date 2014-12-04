(ns clucky.protocols)

(defprotocol Store
  (has? [this k b])
  (add! [this k b])
  (remove! [this k b]))
