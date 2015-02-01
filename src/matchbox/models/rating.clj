(ns matchbox.models.rating
  (:refer-clojure :exclude [sort find])
  (:import (org.bson.types ObjectId)
           (java.util Date))
  (:require [monger.collection :as coll]
            [monger.query :refer :all]
            [matchbox.config :refer [db coll-ratings]]
            [schema.core :as s]))


;; ------------------------------------------------------------------------
;; Schema (used for validation of new objects)

(s/defschema Rating {:_id                         String
                     :item_id                     String
                     :item                        matchbox.models.item/Item
                     :user_id                     String
                     :user                        matchbox.models.user/User
                     :preference                  double
                     (s/optional-key :sentiment)  String
                     (s/optional-key :created_at) Number})

(s/defschema NewRating (dissoc Rating :_id :created_at))

;; ------------------------------------------------------------------------

(defn all []
  (coll/find-maps db coll-ratings))
  ;TODO: limit and sort
  ;(with-collection db coll-ratings
  ;                 (find {})
  ;                 (fields [:score :name])
  ;                 ;; it is VERY IMPORTANT to use array maps with sort
  ;                 (sort (array-map :score -1 :name 1))
  ;                 (limit 10)))


;;(coll/find-maps db coll-ratings))

(defn total []
  (coll/count db coll-ratings))

(defn find-by-user-id [user-id]
  (coll/find-maps db coll-ratings {:user_id user-id} ["item" "item_id" "user" "user_id" "preference" "sentiment" "created_at"]))

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

(defn delete-all []
  (coll/remove db coll-ratings))




;; ========================================================================
;; Populate Initial Data

(defn init-db []
  (let [p1 (matchbox.models.user/create {:alias "wino" :first_name "Wino" :last_name "Voltari"})
        p2 (matchbox.models.user/create {:alias "frank" :first_name "Frank" :last_name "Wurzel"})
        p3 (matchbox.models.user/create {:alias "birne" :first_name "Bernd" :last_name "Birne"})
        p4 (matchbox.models.user/create {:alias "werner" :first_name "Werner" :last_name "Schneeberger"})
        i1 (matchbox.models.item/create {:name "Wine"})
        i2 (matchbox.models.item/create {:name "France"})
        i3 (matchbox.models.item/create {:name "Apple"})
        i4 (matchbox.models.item/create {:name "Snow"})
        i5 (matchbox.models.item/create {:name "Sunshine"})
        i6 (matchbox.models.item/create {:name "Norway"})]

    (create {:item       i1
             :item_id    (str (get i1 :_id))
             :user       p1
             :user_id    (str (get p1 :_id))
             :preference 0.8})
    (create {:item       i4
             :item_id    (str (get i4 :_id))
             :user       p1
             :user_id    (str (get p1 :_id))
             :preference 0.7})
    (create {:item       i2
             :item_id    (str (get i2 :_id))
             :user       p1
             :user_id    (str (get p1 :_id))
             :preference 1.0})
    (create {:item       i6
             :item_id    (str (get i2 :_id))
             :user       p1
             :user_id    (str (get p1 :_id))
             :preference 0.9})

    (create {:item       i1
             :item_id    (str (get i1 :_id))
             :user       p2
             :user_id    (str (get p2 :_id))
             :preference 0.6})
    (create {:item       i3
             :item_id    (str (get i3 :_id))
             :user       p2
             :user_id    (str (get p2 :_id))
             :preference 0.9})
    (create {:item       i4
             :item_id    (str (get i4 :_id))
             :user       p2
             :user_id    (str (get p2 :_id))
             :preference 0.6})
    (create {:item       i2
             :item_id    (str (get i2 :_id))
             :user       p2
             :user_id    (str (get p2 :_id))
             :preference 0.8})
    (create {:item       i5
             :item_id    (str (get i5 :_id))
             :user       p2
             :user_id    (str (get p2 :_id))
             :preference 1.0})

    (create {:item       i3
             :item_id    (str (get i3 :_id))
             :user       p3
             :user_id    (str (get p3 :_id))
             :preference 1.0})
    (create {:item       i1
             :item_id    (str (get i1 :_id))
             :user       p3
             :user_id    (str (get p3 :_id))
             :preference 0.8})
    (create {:item       i4
             :item_id    (str (get i4 :_id))
             :user       p3
             :user_id    (str (get p3 :_id))
             :preference 0.9})
    (create {:item       i5
             :item_id    (str (get i5 :_id))
             :user       p3
             :user_id    (str (get p3 :_id))
             :preference 0.7})

    (create {:item       i1
             :item_id    (str (get i1 :_id))
             :user       p4
             :user_id    (str (get p4 :_id))
             :preference 0.8})
    (create {:item       i4
             :item_id    (str (get i4 :_id))
             :user       p4
             :user_id    (str (get p4 :_id))
             :preference 0.9})
    ))


(if (= total 0)
  (do
    (println "*** No ratings found")
    (init-db))
  (do
    (println "---> Some ratings found")
    (matchbox.models.user/delete-all)
    (matchbox.models.item/delete-all)
    (delete-all)
    (init-db)
    ))

;;(when (empty? (seq all))
;;)
