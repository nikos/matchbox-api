(ns matchbox.models.rating
  (:require [monger.collection :as coll]
            [matchbox.config :refer [db coll-ratings]]))

(defn all []
  (coll/find-maps db coll-ratings))

(defn create [name street]
  (coll/insert db coll-ratings {:name name :street street}))

(defn create-full [rating]
  (coll/insert-and-return db coll-ratings rating))

(defn find-user [user-id]
  (coll/find-one-as-map db coll-ratings {:user-id user-id}))

(defn update [id name street]
  (coll/update db coll-ratings {:_id id} {:name name :street street} {:multi false}))
