(ns matchbox.main-test
  (:use midje.sweet
        matchbox.handler)
  (:require [ring.mock.request :as mock-rq]
            [cheshire.core :as json]))

(comment (fact "Get all users"
      (let [rq (mock-rq/request :get "/users")
            res (app rq)]
        (:status res) => 200
        (:body res) => (get-all-users))))

(fact "Analyze positive sentiment with nouns"
      (let [payload {:sentence "I love New York in summer"}
            rq (mock-rq/request :post "/sentiments/analyze" payload)
            res (app rq)]
        (:status res) => 200
        (json/decode (:body res)) => (contains {"preference" 5.0, "nouns" ["summer" "New York"]}) ))

(fact "Analyze negative sentiment with nouns"
      (let [payload {:sentence "I hate New York in winter"}
            rq (mock-rq/request :post "/sentiments/analyze" payload)
            res (app rq)]
        (:status res) => 200
        (json/decode (:body res)) => (contains {"preference" -5.0, "nouns" ["winter" "New York"]}) ))

(fact "Analyze positive sentiment with verbs"
      (let [payload {:sentence "I love dancing in the rain"}
            rq (mock-rq/request :post "/sentiments/analyze" payload)
            res (app rq)]
        (:status res) => 200
        (json/decode (:body res)) => (contains {"preference" 5.0, "nouns" ["rains"]}) ))


;; (analyze-sentiment payload)


(comment (facts "about the main routes"

       (fact "Bare request returns index file"
             (app-routes {:uri "/" :request-method :get}) => (contains {:status 200 :body "Welcome to matchbox"}))

       "ANO"
       (app-routes {:uri "/users" :request-method :get})
       => (contains {:status 200
                     :body   {:users anything}})

       ))