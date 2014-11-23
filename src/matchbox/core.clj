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
  (let [model (FileDataModel. (File. "test/data/simple-interests.csv"))
        similarity (PearsonCorrelationSimilarity. model)
        neighborhood (NearestNUserNeighborhood. 2 similarity model)
        recommender (GenericUserBasedRecommender. model neighborhood similarity)]

    (println "================")
    (doseq [user-id (range n)]
      (println (str user-id ":" (clojure.string/join "," (.mostSimilarUserIDs recommender user-id k)))))

    (println "================")
    (doseq [user-id (range n)]
      (println user-id)
      (doseq [other-id (range n)]
        (println (str "  " other-id ":" (.userSimilarity similarity user-id other-id))))
      )

    ;; (doseq [user-id (range n)]
    ;;   (spit "zsimilarities.csv" (str user-id ":" (clojure.string/join "," (.mostSimilarUserIDs recommender user-id k)) \newline) :append true)
    ;; )))

    )
  )

(comment
  (find-similar-users 7 3))
