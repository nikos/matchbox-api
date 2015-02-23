(ns matchbox.config
  (:require [monger.core :as mongo]))


(def mongo-host     (or (System/getenv "MONGO_HOST") "localhost"))
(def mongo-port     (Integer/parseInt (or (System/getenv "MONGO_PORT") "27017")))
(def mongo-db       (or (System/getenv "MONGO_DB") "matchbox"))
(def mongo-user     (or (System/getenv "MONGO_USER") "niko"))
(def mongo-password (or (System/getenv "MONGO_PW") ""))


;; Used for building DataSource for Mahout and CRUD
(defonce db-specification
         {:servername mongo-host
          :port       mongo-port
          :database   mongo-db
          :user       mongo-user         ;; TODO use config library from gorillalabs
          :password   mongo-password})

(defonce db-name
         (db-specification :database))

(defonce coll-ratings "ratings")
(defonce coll-users "users")
(defonce coll-items "items")
(defonce coll-sentiments "sentiments")

;; TODO also include connection parameters configurable
(defonce db
         (mongo/get-db (mongo/connect) db-name))
