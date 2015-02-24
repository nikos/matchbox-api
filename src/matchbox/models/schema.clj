(ns matchbox.models.schema
  (:import (org.bson.types ObjectId))
  (:require [schema.core :as s]))

;; -------------------------------------------------------------
;; Schemas (used for validation of new objects)
;; -------------------------------------------------------------

(s/defschema Profile {:name        String
                      :picture     String
                      :initialized boolean})

(s/defschema User {:_id                         ObjectId
                   :profile                     Profile
                   (s/optional-key :created_at) Number})

(s/defschema NewUser (dissoc User :_id :created_at))

;; -------------------------------------------------------------

(s/defschema Item {:_id                         ObjectId
                   :normalized                  String      ;; must be unique (avoid duplicates to ensure recommendation correctness)
                   :name                        String
                   (s/optional-key :created_at) Number})

(s/defschema NewItem (dissoc Item :_id :created_at))

;; ------------------------------------------------------------------------

(s/defschema Rating {:_id                         ObjectId
                     :item_id                     String
                     :item                        Item
                     :user_id                     String
                     :user                        User
                     :preference                  double
                     (s/optional-key :sentiment)  String
                     (s/optional-key :created_at) Number})

(s/defschema NewRating (dissoc Rating :_id :created_at))

;; -------------------------------------------------------------

(s/defschema Sentiment {:_id                         ObjectId
                        :sentence                    String
                        :user_id                     String
                        :user                        User
                        (s/optional-key :ratings)    String ;  TODO should array [matchbox.models.rating/Rating]
                        (s/optional-key :created_at) Number})

(s/defschema NewSentiment (dissoc Sentiment :_id :created_at))
