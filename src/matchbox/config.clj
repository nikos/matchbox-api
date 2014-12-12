(ns matchbox.config)

;; Used for building DataSource for Mahout and CRUD
(def db-specification
  {:servername "localhost"
   :port       27017
   :database   "matchbox"
   :collection "ratings"
   :user       "niko"
   :password   ""})

