(ns matchbox.services.sentiment-analyzer
  (:require [clojure.string :as str]
            [clojure-stemmer.porter.stemmer :as stemmer]
            [matchbox.services.sentiment-train :as tr])
  (:use [opennlp.nlp]
        [clojure.java.io]))


;; -------------------------------------------------------
;; Initialize NLP with english vocabulary
(defonce tokenize (make-tokenizer "models/en-token.bin"))
(defonce detokenize (make-detokenizer "models/english-detokenizer.xml"))
(defonce pos-tag (make-pos-tagger "models/en-pos-maxent.bin"))

;; the actual categorizer function (using OpenNLP)
(def raw-categorize (make-document-categorizer tr/senti-model))

(defn categorize
  "Categorize and post-process to overcome problem, if negative words combined this should leave to a negative category"
  [sentence negatives]
  (let [categorization (raw-categorize sentence)
        category (:best-category categorization)]
    (if (not-empty negatives)
      (str/replace category #"positive" "negative")
      category)))

(defn category2preference
  "Translates verbal categorization into numerical value"
  [category]
  (case category
    "strongsubj-negative" -5.0
    "weaksubj-negative" -2.0
    "weaksubj-positive" 2.0
    "strongsubj-positive" 5.0
    0.0))                         ;; default == neutral

;; -------------------------------------------------------

;; NN     Noun, singular or mass
;; NNS    Noun, plural
;; NNP    Proper noun, singular (z.B. Eigennamen)
;; NNPS   Proper noun, plural
;; ~~
;; VB     Verb, base form
;; VBD    Verb, past tense
;; VBG    Verb, gerund/present participle
;; VBN    Verb, past participle
;; VBP    Verb, non-3rd ps. sing. present
;; VBZ    Verb, 3rd ps. sing. present

(defn stem-nouns  ;; TODO: not only nouns should be stemmed
  "Takes the original categorization list and if any plural nouns (=NNS) found, stem them"
  [arr]
  (map (fn [x]
         (if (= (nth x 1) "NNS")
           (vector (stemmer/stemming (nth x 0)) (nth x 1))    ;; stem first entry, leave type as it is
           (vector (nth x 0) (nth x 1))))                     ;; use original form
       arr))

(defn grab-noun-tuples
  "Returns the array elements, which are all kind of nouns"
  [arr]
  (filter (fn [x]
            (.startsWith (nth x 1) "NN")) arr))

(defn grab-negative-tuples
  "Returns the array elements, which are negative"
  [arr]
  (filter (fn [x]
            (and
              (or
                (= (nth x 0) "n't")
                (= (nth x 0) "not"))
              (.startsWith (nth x 1) "RB"))) arr))

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

(defn- reduce-singles
  "Returns a simple list of single nouns which are not part of the grouped pairs"
  [single-nouns double-nouns]
  (filter #((comp not contains?)
            (set (mapcat (fn [s]
                           (str/split s #" "))
                         double-nouns)) %)
          single-nouns))

;; ~~~~

(defn extract-single-and-double-nouns
  "Returns list of nouns from OpenNLP array,
   extracts single and double nouns"
  [arr]
  (let [single-nouns (grab-nouns arr)
        double-nouns (remove nil? (grab-double-nouns arr))
        reduced-single-nouns (reduce-singles single-nouns double-nouns)]
    (flatten (conj double-nouns reduced-single-nouns))))

;; -------------------------------------------------------

(defn stop-words []
  (set (tr/get-lines "resources/stop_words.txt")))

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