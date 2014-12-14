(ns matchbox.models.item
  (:import (org.bson.types ObjectId))
  (:require [monger.collection :as coll]
            [matchbox.config :refer [db coll-items]]))

(defn all []
  (coll/find-maps db coll-items))

(defn find-by-id [id]
  (coll/find-one-as-map db coll-items {:_id (ObjectId. id)}))

(defn create [item]
  (coll/insert-and-return db coll-items item))

(defn update [id item]
  (coll/update-by-id db coll-items (ObjectId. id) {:name (item :name)} {:multi false}))

(defn delete [id]
  (coll/remove-by-id db coll-items (ObjectId. id)))
