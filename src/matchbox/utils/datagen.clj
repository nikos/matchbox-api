(ns matchbox.datagen
  (:require clojure.string))

;; replace (range 40) with this snippet for string interests:
;; (-> "test/data/interests.csv"
;;                       slurp
;;                       (clojure.string/split #"\n"))
;;                      
;; FileDataModel will need an IDMigrator
;; http://kickstarthadoop.blogspot.com/2011/05/mahout-recommendations-with-data-sets.html
;;
;; uses random preference [0, 1) as preference, could score based on order interests were added by user
(defn gen-users-with-interests
  "Generates n users with 0..k interests from a set of j possible."
  [n k j]
  (let [interests (range j)]
    (for [user-id (range n)
          user-interest (take (rand-int k) (shuffle interests))]

      (spit "test/data/users-interests.csv"
            (str user-id "," user-interest "," (rand) \newline) :append true))))

(comment
  (gen-users-with-interests 1000 8 40))

