(ns matchbox.models.rating
  (:require [monger.core :as mongo]
            [monger.collection :as coll]
            [matchbox.config :as config]))

(def dbname
  (get config/db-specification :database))

(def collname
  (get config/db-specification :collection))


;; TODO make connection parameters configurable
(def get-db
  (mongo/get-db (mongo/connect) dbname))


(defn all []
  (coll/find-maps get-db collname))

(defn create [name street]
  (coll/insert get-db collname {:name name :street street}))

(defn find-user [user-id]
  (coll/find-one-as-map get-db collname {:user-id user-id}))

(defn update [id name street]
  (coll/update get-db collname {:_id id} {:name name :street street} {:multi false}))
