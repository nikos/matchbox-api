(ns matchbox.handler
  (:use compojure.core)
  (:use cheshire.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [monger.json]
            [matchbox.services.recommender :as recommender]
            [matchbox.models.rating :as rating]
            [matchbox.models.user :as user]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]))


(defn get-ratings
  "Retrieve all ratings from the database"
  []
  (response {:ratings (rating/all)}))


(defn get-all-users []
  (response {:users (user/all)}))

(defn create-new-user [doc]
  (response (user/create-full doc)))


(defn get-similar-users
  "Looks up similar user via mahout"
  [user-id]
  {:recommended (recommender/find-similar-users 3 user-id)})


(defroutes app-routes
           (context "/users" []
                    (defroutes users-routes
                               (GET "/" [] (get-all-users))
                               (POST "/" {body :body} (create-new-user body))))

           (GET "/" []
                (str "Huhu World" "Test"))
           (GET "/tastes.json" [] (get-ratings))
           (GET "/test.json" []
                (response {:nickname "getMessages" :summary "Get message"}))
           (GET "/sim/:user-id" [user-id]
                (response (get-similar-users user-id)))
           (route/not-found
                "Not Found"))

;; Bootstraps the API (refer by project.clj)
(def app
  (-> (handler/api app-routes)                  ;; TODO: doch lieber wieder zurück zu defaults? (01-12)
      (wrap-with-logger)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
