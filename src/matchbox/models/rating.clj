(ns matchbox.models.rating
  (:refer-clojure :exclude [sort find])
  (:import (org.bson.types ObjectId)
           (java.util Date))
  (:require [monger.collection :as coll]
            [monger.query :refer :all]
            [matchbox.models.user :as user]
            [matchbox.models.item :as item]
            [matchbox.config :refer [db coll-ratings]]))


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
  (let [p1 (user/create {:profile {:name "Wino Voltari"}})
        p2 (user/create {:profile {:name "Frank Wurzel"}})
        p3 (user/create {:profile {:name "Bernd Birne"}})
        p4 (user/create {:profile {:name "Werner Schneeberger"}})
        i1 (item/create {:name "Wine"})
        i2 (item/create {:name "France"})
        i3 (item/create {:name "Apple"})
        i4 (item/create {:name "Snow"})
        i5 (item/create {:name "Sunshine"})
        i6 (item/create {:name "Norway"})]

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
