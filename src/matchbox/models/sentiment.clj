(ns matchbox.models.sentiment
  (:import (org.bson.types ObjectId))
  (:require [monger.collection :as coll]
            [opennlp.nlp :refer [make-tokenizer make-pos-tagger]]
            [matchbox.config :refer [db coll-sentiments]]
            [schema.core :as s]))

;; -------------------------------------------------------
;; Initialize NLP with english vocabulary
(defonce tokenize (make-tokenizer "models/en-token.bin"))
(defonce pos-tag (make-pos-tagger "models/en-pos-maxent.bin"))

;; -------------------------------------------------------------
;; Schema (used for validation of new objects)

(s/defschema Sentiment {:_id                         ObjectId
                        :sentence                    String
                        :user_id                     String
                        :user                        matchbox.models.user/User
                        (s/optional-key :ratings)    String ;matchbox.models.rating/Rating
                        (s/optional-key :created_at) Number})

(s/defschema NewSentiment (dissoc Sentiment :_id :created_at))

;; -------------------------------------------------------------

(defn all []
  (coll/find-maps db coll-sentiments))

(defn find-by-id [id]
  (coll/find-one-as-map db coll-sentiments {:_id (ObjectId. id)}))

(defn create [sentiment]
  (clojure.pprint/pprint (pos-tag (tokenize (:sentence sentiment))))
  (coll/insert-and-return db coll-sentiments sentiment))

(defn delete [id]
  (coll/remove-by-id db coll-sentiments (ObjectId. id)))

(defn delete-all []
  (coll/remove db coll-sentiments))

