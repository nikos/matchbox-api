(ns matchbox.models.item
  (:import (org.bson.types ObjectId))
  (:require [monger.collection :as coll]
            [matchbox.config :refer [db coll-items]]
            [schema.core :as s]))

(def Item {:name                        String              ;; must be unique
           (s/optional-key :_id)        String
           (s/optional-key :created_at) Number})

(defn all []
  (coll/find-maps db coll-items))

(defn find-by-id [id]
  (coll/find-one-as-map db coll-items {:_id (ObjectId. id)}))

(defn find-by-name [name]
  (coll/find-one-as-map db coll-items {:name name}))

(defn create [item]
  (coll/insert-and-return db coll-items item))

(defn update [id item]
  (coll/update-by-id db coll-items (ObjectId. id) {:name (item :name)} {:multi false}))

(defn delete [id]
  (coll/remove-by-id db coll-items (ObjectId. id)))