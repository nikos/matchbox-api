(ns matchbox.config
 (:require [monger.core :as mongo]))

;; Used for building DataSource for Mahout and CRUD
(def db-specification
  {:servername "localhost"
   :port       27017
   :database   "matchbox"
   :user       "niko"
   :password   ""})

(def db-name
 (db-specification :database))

(def coll-ratings "ratings")
(def coll-users "users")

;; TODO also include connection parameters configurable
(def db
 (mongo/get-db (mongo/connect) db-name))
