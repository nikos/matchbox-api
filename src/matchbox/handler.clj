(ns matchbox.handler
  (:use compojure.core)
  (:use cheshire.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [monger.json]
            [matchbox.services.recommender :as recommender]
            [matchbox.models.rating :as rating]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]))


(defstruct taste :user_id :item_id :preference :created_at)

(defn get-tastes
  "Retrieve all tastes from the database"
  []
  (response {:results (rating/all)}))


(defn get-similar-users
  "Looks up similar user via mahout"
  [user-id]
  {:recommended (recommender/find-similar-users 3 user-id)})


(defroutes app-routes
           (GET "/" []
                (str "Huhu World" "Test"))
           (GET "/tastes.json" [] (get-tastes))
           (GET "/test.json" []
                (response {:nickname "getMessages" :summary "Get message"}))
           (GET "/sim/:user-id" [user-id]
                (response (get-similar-users user-id)))
           (route/not-found
                "Not Found"))

;; TODO: Add logging: https://github.com/pjlegato/ring.middleware.logger
(def app
  (-> (handler/api app-routes)                  ;; TODO: doch lieber wieder zurück zu defaults? (01-12)
      (wrap-with-logger)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
