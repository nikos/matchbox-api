(ns matchbox.core
  (:import (org.apache.mahout.cf.taste.impl.similarity PearsonCorrelationSimilarity))
  (:require clojure.string
            [matchbox.config :as config])
  (:import  org.apache.mahout.cf.taste.impl.model.mongodb.MongoDBDataModel
            org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
            org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
            org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender))


(defn get-datamodel
  "INTERNAL: Retrieve access to data model"
  []
  (MongoDBDataModel. (get config/db-specification :servername)
                     (get config/db-specification :port)
                     (get config/db-specification :database)
                     (get config/db-specification :collection)
                     false false nil)                       ; manage finalRemove dateFormat
  )

(defn id-to-long
  "From User Object-ID (String) to a Long Value"
  [model id]
  (Long/parseLong (.fromIdToLong model id true)))

(defn long-to-id
  "From Long Value to an User Object-ID (String)"
  [model id]
  (.fromLongToId model id))


(defn find-similar-users
  "For n most similar users to given user-id."
  [n user-id]
  (let [model (get-datamodel)
        similarity (PearsonCorrelationSimilarity. model)
        neighborhood (NearestNUserNeighborhood. 2 similarity model)
        recommender (GenericUserBasedRecommender. model neighborhood similarity)
        long-id (id-to-long model user-id)
        similar-users (.mostSimilarUserIDs recommender long-id n)]

    (println (str user-id ":" (clojure.string/join "," similar-users)))
    (println "================")
    (doseq [other-id similar-users]
      (println (str "  OTHER: " (long-to-id model other-id) ":")))
    ;;(println (str "  " (other-id) ":" (.userSimilarity similarity long-id other-id)))
    (str user-id ":" (clojure.string/join "," similar-users))
    )
  )

(comment
  (find-similar-users 3 0))
