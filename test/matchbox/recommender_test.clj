(ns matchbox.recommender-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [matchbox.services.recommender :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(fact "simple fact"
      {:x 1, :y 1} => {:y 1, :x 1})