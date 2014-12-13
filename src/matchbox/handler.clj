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
            [ring.util.response :refer [response not-found]]))

;; -------------------------------------------------------

(defn get-all-ratings []
  (response {:ratings (rating/all)}))

(defn get-rating [id]
  (response {:rating (rating/find-by-id id)}))

(defn create-new-rating [doc]
  (response (rating/create doc)))

(defn update-rating [id doc]
  (let [rating (rating/find-by-id id)]
    (cond
      (empty? rating) (not-found "User does not exist")
      :else (do (rating/update id doc)
                (response "OK")))))

(defn delete-rating [id]
  (let [rating (rating/find-by-id id)]
    (cond
      (empty? rating) (not-found "User does not exist")
      :else (do (rating/delete id)
                (response "OK")))))

;; -------------------------------------------------------

(defn get-all-users []
  (response {:users (user/all)}))

(defn get-user [id]
  (response {:user (user/find-by-id id)}))

(defn create-new-user [doc]
  (response (user/create doc)))

(defn update-user [id doc]
  (let [user (user/find-by-id id)]
    (cond
      (empty? user) (not-found "User does not exist")
      :else (do (user/update id doc)
                (response "OK")))))

(defn delete-user [id]
  (let [user (user/find-by-id id)]
    (cond
      (empty? user) (not-found "User does not exist")
      :else (do (user/delete id)
                (response "OK")))))

(defn get-similar-users
  "Looks up similar users via mahout recommendation"
  [user-id]
  (response {:recommended (recommender/find-similar-users 3 user-id)}))


(defroutes app-routes

           (GET "/" [] (str "Welcome to matchbox"))

           (context "/users" []
                    (defroutes users-routes
                               (GET "/" [] (get-all-users))
                               (POST "/" {body :body} (create-new-user body))
                               (context "/:id" [id] (defroutes user-routes
                                                               (GET "/similar-users" [] (get-similar-users id))
                                                               (GET "/" [] (get-user id))
                                                               (PUT "/" {body :body} (update-user id body))
                                                               (DELETE "/" [] (delete-user id))))))

           (context "/ratings" []
                    (defroutes ratings-routes
                               (GET "/" [] (get-all-ratings))
                               (POST "/" {body :body} (create-new-rating body))
                               (context "/:id" [id] (defroutes rating-routes
                                                               (GET "/" [] (get-rating id))
                                                               (PUT "/" {body :body} (update-rating id body))
                                                               (DELETE "/" [] (delete-rating id))))))

           (route/not-found "Not Found"))

;; Bootstraps the API (refer by project.clj)
(def app
  (-> (handler/api app-routes)                              ;; TODO: doch lieber wieder zurück zu defaults? (01-12)
      (wrap-with-logger)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
