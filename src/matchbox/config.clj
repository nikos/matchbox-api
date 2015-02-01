(ns matchbox.config
  (:require [monger.core :as mongo]))

;; Used for building DataSource for Mahout and CRUD
(defonce db-specification
         {:servername "localhost"
          :port       27017
          :database   "matchbox"
          :user       "niko"     ;; TODO use config library from gorillalabs
          :password   ""})

(defonce db-name
         (db-specification :database))

(defonce coll-ratings "ratings")
(defonce coll-users "users")
(defonce coll-items "items")
(defonce coll-sentiments "sentiments")

;; TODO also include connection parameters configurable
(defonce db
         (mongo/get-db (mongo/connect) db-name))
