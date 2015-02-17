(ns matchbox.main-test
  (:use midje.sweet
        matchbox.handler)
  (:require [ring.mock.request :as mock-rq]))

(fact "root loads index"
      (let [rq (mock-rq/request :get "/users")
            res (app rq)]
        (:status res) => 200
        (:body res) => (get-all-users)))

(facts "about the main routes"

       (fact "Bare request returns index file"
             (app-routes {:uri "/" :request-method :get}) => (contains {:status 200 :body "Welcome to matchbox"}))

       "ANO"
       (app-routes {:uri "/users" :request-method :get})
       => (contains {:status 200
                     :body   {:users anything}})

       )