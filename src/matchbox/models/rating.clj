(ns matchbox.models.rating
  (:require [monger.core :as mongo]
            [monger.collection :as coll]))

;; TODO make connection parameters configurable
(def get-db
  (mongo/get-db (mongo/connect) "matchbox"))

;; Only for testing
(let [db get-db
      coll "ratings"]
  (coll/insert db coll {:first_name "John" :last_name "Lennon"})
  (coll/insert db coll {:first_name "Ringo" :last_name "Starr"})

  (coll/find db coll {:first_name "Ringo"}))

(defn all []
  (coll/find-maps get-db "ratings"))

(defn create [name street]
  (coll/insert get-db "ratings" {:name name :street street}))

(defn find-user [user-id]
  (coll/find-one-as-map get-db "ratings" {:user-id user-id}))

(defn update [id name street]
  (coll/update get-db "ratings" {:_id id} {:name name :street street} {:multi false}))
