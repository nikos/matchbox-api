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


(defn get-all-ratings []
  (response {:ratings (rating/all)}))

(defn create-new-rating [doc]
  (response (rating/create-full doc)))


(defn get-all-users []
  (response {:users (user/all)}))

(defn get-user [id]
  (response {:user (user/find-by-id id)}))

(defn create-new-user [doc]
  (response (user/create doc)))

(defn update-user [id doc]
  (response {:user (user/update id doc)}))

(defn delete-user [id]
  (user/delete id)
  (response "OK"))


(defn get-similar-users
  "Looks up similar user via mahout"
  [user-id]
  (response {:recommended (recommender/find-similar-users 3 user-id)}))


(defroutes app-routes

           (GET "/" [] (str "Welcome to matchbox"))
           (GET "/test.json" []
                (response {:nickname "getMessages" :summary "Get message"}))

           (context "/users" []
                (defroutes users-routes
                     (GET "/" [] (get-all-users))
                     (POST "/" {body :body} (create-new-user body))
                     (context "/:id" [id] (defroutes user-routes
                           (GET    "/" [] (get-user id))
                           (PUT    "/" {body :body} (update-user id body))
                           (DELETE "/" [] (delete-user id))))))

           (context "/ratings" []
                    (defroutes ratings-routes
                               (GET "/" [] (get-all-ratings))
                               (POST "/" {body :body} (create-new-rating body))
                               (GET "/similar/:user-id" [user-id] (get-similar-users user-id))))

           (route/not-found
             "Not Found"))

;; Bootstraps the API (refer by project.clj)
(def app
  (-> (handler/api app-routes)                              ;; TODO: doch lieber wieder zurück zu defaults? (01-12)
      (wrap-with-logger)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
