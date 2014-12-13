(ns matchbox.models.user
  (:import (org.bson.types ObjectId))
  (:require [monger.collection :as coll]
            [matchbox.config :refer [db coll-users]]))

(defn all []
  (coll/find-maps db coll-users))

(defn find-by-id [id]
  (coll/find-one-as-map db coll-users {:_id (ObjectId. id)}))

(defn create [user]
  (coll/insert-and-return db coll-users user))

(defn update [id user]
  (coll/update-by-id db coll-users (ObjectId. id) {:name (user :name)} {:multi false}))

(defn delete [id]
  (coll/remove-by-id db coll-users (ObjectId. id)))
