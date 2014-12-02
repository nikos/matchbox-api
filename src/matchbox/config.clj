(ns matchbox.config
  (:import (com.mysql.jdbc.jdbc2.optional MysqlConnectionPoolDataSource)))

(def production?
  (= "production" (System/getenv "RING_ENV")))

(def development?
  (not production?))

(def db-connection-params
  (if production?
    (System/getenv "DATABASE_URL")
    {:classname   "com.mysql.jdbc.Driver"
     :subprotocol "mysql"
     :subname     "//localhost:3306/matchbox"
     :user        "niko"
     :password    ""}))

;; Used for building DataSource for Mahout (TODO: harmonize)
(def db-specification
  {:servername "localhost"
   :database   "matchbox"
   :user       "niko"
   :password   ""})

(defn get-datasource
  "Get JDBC DataSource (for proper data access)"
  [specification]
  (let [datasource (MysqlConnectionPoolDataSource.)]  ;; TODO com.mchange.v2.c3p0.DataSources  DataSources/pooledDataSource
    (.setServerName datasource (:servername specification))
    (.setDatabaseName datasource (:database specification))
    (.setUser datasource (:user specification))
    (.setPassword datasource (:password specification))
    datasource))

(def datasource (get-datasource db-specification))
