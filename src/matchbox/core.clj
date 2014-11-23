(ns matchbox.core
  (:require clojure.string)
  (:import [org.apache.mahout.cf.taste.impl.model.jdbc MySQLJDBCDataModel]
           [org.apache.mahout.cf.taste.impl.similarity PearsonCorrelationSimilarity]
           [org.apache.mahout.cf.taste.impl.neighborhood NearestNUserNeighborhood]
           [org.apache.mahout.cf.taste.impl.recommender GenericUserBasedRecommender]
           [com.mysql.jdbc.jdbc2.optional MysqlDataSource]))

(def specification {
                    :servername "localhost"
                    :database "matchbox"
                    :user "niko"
                    :password ""
                    })

(defn get-datasource
  "Get JDBC DataSource (for proper data access)"
  [specification]
  (let [datasource (MysqlDataSource.)]
    (.setServerName datasource (:servername specification))
    (.setDatabaseName datasource (:database specification))
    (.setUser datasource (:user specification))
    (.setPassword datasource (:password specification))
    datasource
    ))

(defn find-similar-users
  "For n users find the k most similar."
  [datasource n k]
  (let [model (MySQLJDBCDataModel. datasource)
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

;; (get-datasource specification)

(comment
  (find-similar-users datasource 7 3))
