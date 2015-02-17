(ns matchbox.models.sentiment
  (:import (org.bson.types ObjectId))
  (:require [monger.collection :as coll]
            [matchbox.services.sentiment-analyzer :as sent]
            [matchbox.config :refer [db coll-sentiments]]))


(defn all []
  (coll/find-maps db coll-sentiments))

(defn find-by-id [id]
  (coll/find-one-as-map db coll-sentiments {:_id (ObjectId. id)}))

(defn create [sentiment]
  (clojure.pprint/pprint (sent/pos-tag (sent/tokenize (:sentence sentiment))))
  (coll/insert-and-return db coll-sentiments sentiment))

(defn delete [id]
  (coll/remove-by-id db coll-sentiments (ObjectId. id)))

(defn delete-all []
  (coll/remove db coll-sentiments))
