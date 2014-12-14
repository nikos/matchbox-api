(ns matchbox.models.rating
  (:import (org.bson.types ObjectId)
           (java.util Date))
  (:require [monger.collection :as coll]
            [matchbox.config :refer [db coll-ratings]]
            [schema.core :as s]))

(def Rating {:item_id                     String
             :user_id                     String
             :preference                  double
             (s/optional-key :_id)        String
             (s/optional-key :created_at) Number})

(defn all []
  (coll/find-maps db coll-ratings))

(defn find-by-user-id [user-id]
  (coll/find-maps db coll-ratings {:user_id user-id} ["item_id" "preference" "created_at"]))

(defn find-by-id [id]
  (coll/find-one-as-map db coll-ratings {:_id (ObjectId. id)}))

(defn create [rating]
  (let [now (Date.)
        ;; MongoDB timestamps are only precise up to the second
        timestamp (Math/round (double (/ (.getTime now) 1000)))]
    (coll/insert-and-return db coll-ratings (assoc rating :created_at timestamp))))

(defn update [id rating]
  (coll/update-by-id db coll-ratings (ObjectId. id)
                     {:item_id (rating :item_id) :user_id (rating :user_id) :preference (rating :preference)}
                     {:multi false}))

(defn delete [id]
  (coll/remove-by-id db coll-ratings (ObjectId. id)))
