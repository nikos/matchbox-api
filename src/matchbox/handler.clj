(ns matchbox.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn my-calc
  "Just testing"
  []
  (* 5 25))

(defroutes app-routes
  (GET "/" [] (str "Huhu World" (my-calc)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
