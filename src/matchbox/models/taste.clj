(ns matchbox.models.taste
  (:require [clojure.java.jdbc :as jdbc]))

(defn all []
  (jdbc/with-query-results results
                           ["select * from addresses order by name"]
                           (into [] results)))

(defn create [name street]
  (jdbc/insert-record :addresses {:name name :street street}))

(defn find-id [id]
  (jdbc/with-query-results results
                           ["select * from addresses where id = ?" id]
                           (first results)))

(defn update [id name street]
  (jdbc/update-values :addresses ["id = ?" id] {:name name :street street}))
