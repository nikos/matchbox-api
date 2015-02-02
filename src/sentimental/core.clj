(ns sentimental.core
  (:require [clojure-stemmer.porter.stemmer :as stemmer]
            [sentimental.train :as tr])
  (:use [opennlp.nlp]
        [clojure.java.io]))


;; -------------------------------------------------------
;; Initialize NLP with english vocabulary
(defonce tokenize (make-tokenizer "models/en-token.bin"))
(defonce detokenize (make-detokenizer "models/english-detokenizer.xml"))
(defonce pos-tag (make-pos-tagger "models/en-pos-maxent.bin"))

; the actual categorizer
(def categorize (make-document-categorizer tr/senti-model))

(defn stop-words []
  (set (sentimental.train/get-lines "resources/stop_words.txt")))

(defn strip-stop-words [l]
  (filter (fn [x] (not (contains? (stop-words) x)))
          (set l)))

(defn bag-of-words
  "Converts a string into a set of unique words/elements,
  each stemmed (in English)"
  [s]
  (set (map (fn [x] (stemmer/stemming x))
            (strip-stop-words (tokenize s)))))

(defn compact
  "Takes a string, strips out stop words, and stems each word.
  Returns a string"
  [s]
  (detokenize (bag-of-words s)))