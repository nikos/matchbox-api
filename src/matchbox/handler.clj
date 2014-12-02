(ns matchbox.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [matchbox.core :as mc]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]))


(defn my-calc
  "Just testing"
  []
  (* 5 25))


(defn get-similar-users
  "Looks up similar user via mahout"
  [user-id]
  {:recommended (mc/find-similar-users 3 user-id)})


(defroutes app-routes
           (GET "/" []
                (str "Huhu World" (my-calc)))
           (GET "/test.json" []
                (response {:nickname "getMessages" :summary "Get message"}))
           (GET "/sim/:user-id" [user-id]
                (response (get-similar-users (Integer/parseInt user-id))))
           (route/not-found
                "Not Found"))

;; TODO: Add logging: https://github.com/pjlegato/ring.middleware.logger
(def app
  (-> (handler/api app-routes)                   ;; TODO: doch lieber wieder zurück zu defaults? (01-12)
      (wrap-with-logger)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
