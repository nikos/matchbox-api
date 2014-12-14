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

(defn generic-update [id doc lookup update-fn]
  (cond
    (empty? lookup) (not-found "Object does not exist")
    :else (do (update-fn id doc)
              (response "OK"))))

(defn generic-delete [id lookup delete-fn]
  (cond
    (empty? lookup) (not-found "Object does not exist")
    :else (do (delete-fn id)
              (response "OK"))))

;; -------------------------------------------------------

(defn get-all-ratings []
  (response {:ratings (rating/all)}))

(defn get-rating [id]
  (response {:rating (rating/find-by-id id)}))

(defn create-new-rating [doc]
  (response (rating/create doc)))

(defn update-rating [id doc]
  (generic-update id doc (rating/find-by-id id)
                  (fn [id doc] (rating/update id doc))))

(defn delete-rating [id]
  (generic-delete id (rating/find-by-id id)
                  (fn [id] (rating/delete id))))

;; -------------------------------------------------------

(defn get-all-users []
  (response {:users (user/all)}))

(defn get-user [id]
  (response {:user (user/find-by-id id)}))

(defn create-new-user [doc]
  (response (user/create doc)))

(defn update-user [id doc]
  (generic-update id doc (user/find-by-id id)
                  (fn [id doc] (user/update id doc))))

(defn delete-user [id]
  (generic-delete id (user/find-by-id id)
                  (fn [id] (user/delete id))))

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
