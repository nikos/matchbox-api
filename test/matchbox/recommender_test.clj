(ns matchbox.recommender-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [matchbox.services.recommender :refer :all]
            [matchbox.services.sentiment-analyzer :as sent]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(fact "simple fact"
      {:x 1, :y 1} => {:y 1, :x 1})


(defn simplified-analysis
  [sentence]
  (let [tok-sentence (sent/pos-tag (sent/tokenize sentence))
        nouns (sent/extract-single-and-double-nouns tok-sentence)
        ] ;; normalized (sent/normalize nouns)
    nouns))

;; for each noun (pair) tokenize it and then stem-it
;; see bag-of-words


(fact "Test bag of words (with stemming)"
      (sent/bag-of-words "I like drinking cidre in the morning") => #{"I" "cidr" "drink" "morn"})

(fact "Grab nouns from sentence"
      (let [sentence "I like San Francisco in the winter"
            tok-sentence (sent/pos-tag (sent/tokenize sentence))
            nouns (sent/grab-noun-tuples tok-sentence)]
        nouns => (list ["San" "NNP"] ["Francisco" "NNP"] ["winter" "NN"])))


(fact "Grab two consequtive nouns in a row from sentence"
      (let [sentence "I like San Francisco in the winter"
            tok-sentence (sent/pos-tag (sent/tokenize sentence))
            nouns (sent/grab-double-nouns tok-sentence)]
        nouns => (list ["San Francisco" "NNP"] ["winter" "NN"])))


(fact "Positive sentiments"
      (simplified-analysis "I love New York in winter") => (list "winter" "New York")
      (simplified-analysis "I like drinking beer") => (list "beer"))

