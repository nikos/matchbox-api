(ns matchbox.models.rating
  (:import (org.bson.types ObjectId))
  (:require [monger.collection :as coll]
            [matchbox.config :refer [db coll-ratings]]))

(defn all []
  (coll/find-maps db coll-ratings))

(defn find-by-id [id]
  (coll/find-one-as-map db coll-ratings {:_id (ObjectId. id)}))

(defn create [rating]
  (coll/insert-and-return db coll-ratings rating))

(defn update [id rating]
  (coll/update-by-id db coll-ratings (ObjectId. id) {:name (rating :name)} {:multi false}))

(defn delete [id]
  (coll/remove-by-id db coll-ratings (ObjectId. id)))
