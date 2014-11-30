(ns matchbox.core
  (:require clojure.string)
  (:use [matchbox.config :as config])
  (:import [org.apache.mahout.cf.taste.impl.model.jdbc MySQLJDBCDataModel]
           [org.apache.mahout.cf.taste.impl.similarity PearsonCorrelationSimilarity]
           [org.apache.mahout.cf.taste.impl.neighborhood NearestNUserNeighborhood]
           [org.apache.mahout.cf.taste.impl.recommender GenericUserBasedRecommender]))

(defn find-similar-users
  "For n most similar users to given user-id."
  [n user-id]
  (let [model (MySQLJDBCDataModel. config/datasource)
        similarity (PearsonCorrelationSimilarity. model)
        neighborhood (NearestNUserNeighborhood. 2 similarity model)
        recommender (GenericUserBasedRecommender. model neighborhood similarity)
        similar-users (.mostSimilarUserIDs recommender user-id n)]

    (println (str user-id ":" (clojure.string/join "," similar-users)))
    (println "================")
    (doseq [other-id similar-users]
      (println (str "  " other-id ":" (.userSimilarity similarity user-id other-id))))
    (str user-id ":" (clojure.string/join "," similar-users))
    )
  )

(comment
  (find-similar-users 3 0))
