(ns matchbox.core
  (:require clojure.string)
  (:import [org.apache.mahout.cf.taste.eval RecommenderBuilder]
           [org.apache.mahout.cf.taste.impl.model.file FileDataModel]
           [org.apache.mahout.cf.taste.impl.similarity PearsonCorrelationSimilarity]
           [org.apache.mahout.cf.taste.impl.neighborhood NearestNUserNeighborhood]
           [org.apache.mahout.cf.taste.impl.recommender GenericUserBasedRecommender]
           [org.apache.mahout.cf.taste.impl.eval AverageAbsoluteDifferenceRecommenderEvaluator]
           [java.io File]))

(defn find-similar-users
  "For n users find the k most similar."
  [n k]
  (let [model (FileDataModel. (File. "test/data/users-interests.csv"))
        similarity (PearsonCorrelationSimilarity. model)
        neighborhood (NearestNUserNeighborhood. 2 similarity model)
        recommender  (GenericUserBasedRecommender. model neighborhood
                                                   similarity)]
    (doseq [user-id (range n)]
      (spit "similarities.csv" (str user-id ":" (clojure.string/join "," (.mostSimilarUserIDs recommender user-id k) )  \newline) :append true))))

(comment
  (find-similar-users 1000 3))
