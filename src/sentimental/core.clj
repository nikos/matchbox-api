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

;; the actual categorizer (using OpenNLP)
(def categorize (make-document-categorizer tr/senti-model))
;; -------------------------------------------------------

;; (1)
(defn stem-nouns
  "Takes the original categorization list and if any singular nouns (=NN) stem them"
  [arr]
  (map (fn [x]
         (if (= (nth x 1) "NN")
           (vector (stemmer/stemming (nth x 0)) (nth x 1))
           (vector (nth x 0) (nth x 1))))
       arr))

;; NN    Noun, singular or mass
;; NNP   Proper noun, singular
;; NNPS  Proper noun, plural
;; NNS   Noun, plural

(defn grab-noun-tuples
  "Returns the array elements, which are all kind of nouns"
  [arr]
  (filter (fn [x]
            (.startsWith (nth x 1) "NN")) arr))

(defn grab-nouns
  "Flatten the array elements in a simple list of nouns"
  [arr]
  (map #(first %) (grab-noun-tuples arr)))

(defn grab-double-nouns
  "Group nouns in pairs, if they directly occur after each other"
  [arr]
  ;; TODO can possibly improved to not make use of for loop?
  (for [i (range 0 (- (count arr) 1))]
    (let [curr-type (nth (nth arr i) 1)
          curr-val (nth (nth arr i) 0)
          nxt-type (nth (nth arr (+ i 1)) 1)
          nxt-val (nth (nth arr (+ i 1)) 0)]
      (if
        (and
          (.startsWith curr-type "NN")
          (.startsWith nxt-type "NN"))
        (str curr-val " " nxt-val)))))

(defn reduce-singles
  "Returns a simple list of single nouns which are not part of the grouped pairs"
  [single-nouns double-nouns]
  (filter #((comp not contains?)
            (set (mapcat (fn [s]
                           (str/split s #" "))
                         double-nouns)) %)
          single-nouns))

;; ---

(defn extract-single-and-double-nouns
  "PUBLIC: Returns list of nouns from OpenNLP array,
   Combines stemming, extracting single and double nouns"
  [arr]
  (let [stemmed-arr (stem-nouns arr)
        single-nouns (grab-nouns stemmed-arr)
        double-nouns (remove nil? (grab-double-nouns stemmed-arr))
        reduced-single-nouns (reduce-singles single-nouns double-nouns)]
    (flatten (conj double-nouns reduced-single-nouns))))


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