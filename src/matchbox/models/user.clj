(ns matchbox.models.user
  (:require [monger.collection :as coll]
            [matchbox.config :refer [db coll-users]]))

(defn all []
  (coll/find-maps db coll-users))

(defn create [name street]
  (coll/insert db coll-users {:name name :street street}))

(defn create-full [user]
  (coll/insert-and-return db coll-users user))

(defn find-user [user-id]
  (coll/find-one-as-map db coll-users {:user-id user-id}))

(defn update [id name street]
  (coll/update db coll-users {:_id id} {:name name :street street} {:multi false}))
