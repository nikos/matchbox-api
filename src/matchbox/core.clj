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
  "For n most similar users to given user-id."
  [datasource n user-id]
  (let [model (MySQLJDBCDataModel. datasource)
        similarity (PearsonCorrelationSimilarity. model)
        neighborhood (NearestNUserNeighborhood. 2 similarity model)
        recommender (GenericUserBasedRecommender. model neighborhood similarity)
        similar-users (.mostSimilarUserIDs recommender user-id n)]

    (println (str user-id ":" (clojure.string/join "," similar-users)))
    (println "================")
    (doseq [other-id similar-users]
        (println (str "  " other-id ":" (.userSimilarity similarity user-id other-id))))
    )
  )


(def datasource (get-datasource specification))

(comment
  (find-similar-users datasource 3 0))
