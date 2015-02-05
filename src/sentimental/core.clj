(ns sentimental.core
  (:require [clojure.string :as str]
            [clojure-stemmer.porter.stemmer :as stemmer]
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

;; -------------------------------------------------------

; TODO need to stem

(map (fn [x]
       (vector (stemmer/stemming (nth x 0)) (nth x 1))) arr)


(defn grab-noun-tuples
  "Returns the tuples, which are nouns"
  [arr]
  (filter (fn [x]
            (.startsWith (nth x 1) "NN")) arr))
(grab-noun-tuples arr)

(defn grab-nouns
  "Out of the tuples only return a list of nouns"
  [arr]
  (map #(first %)
       (filter (fn [x]
                 (.startsWith (nth x 1) "NN")) arr)))
(grab-nouns arr)
(def my-singles (grab-nouns arr))

(defn grab-double-nouns
  [arr]
  (for [i (range 0 (- (count arr) 1))]
    (let [curr-type (nth (nth arr i) 1)
          curr-val (nth (nth arr i) 0)
          nxt-type (nth (nth arr (+ i 1)) 1)
          nxt-val (nth (nth arr (+ i 1)) 0)]
      (if
        (and
          (= curr-type "NN")
          (= nxt-type "NN"))
        (str curr-val " " nxt-val)
        ))))
(grab-double-nouns arr)
(def my-doubles (remove nil? (grab-double-nouns arr)))

(defn reduce-singles
  [single-nouns double-nouns]
  (filter  #((comp not contains?)
             (set (mapcat (fn[s]
                            (str/split s #" "))
                          double-nouns)) %)
           single-nouns))

(reduce-singles my-singles my-doubles)


;; -------------------------------------------------------

(defn get-nouns-tuples
  "Returns the nouns tuples from the tokenized list"
  [tok-sentence]
  (filter (fn [x] (.startsWith (nth x 1) "NN")) tok-sentence))

(defn get-nouns
  "Returns a list of nouns"
  [tok-sentence]
  (map (fn [x] (nth x 0))
       (get-nouns-tuples tok-sentence)))

(defn get-stemmed-nouns
  [tok-sentence]
  (map (fn [x] (stemmer/stemming x))
       (get-nouns tok-sentence)))

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