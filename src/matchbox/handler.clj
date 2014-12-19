(ns matchbox.handler
  (:import (clojure.lang ExceptionInfo))
  (:use compojure.core)
  (:use cheshire.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [monger.json]
            [matchbox.services.recommender :as recommender]
            [matchbox.models.user :as user]
            [matchbox.models.item :as item]
            [matchbox.models.rating :as rating]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [not-found]]
            [schema.core :as s]))

;; -------------------------------------------------------

(defn client-error
  "Returns a 400 'Bad Request' response."
  [body]
  {:status  400
   :headers {}
   :body    body})

(defn response-ok
  "Returns a skeletal Ring response with the given body, status of 200, and no
  headers."
  [body]
  {:status  200
   :headers { "Access-Control-Allow-Origin" "*" }
   :body    body})

(defn created-ok
  "Returns a Ring response for a HTTP 201 created response."
  {:added "1.2"}
  ([url] (created-ok url nil))
  ([url body]
    {:status  201
     :headers {"Location" url
               "Access-Control-Allow-Origin" "*"}
     :body    body}))
;; -------------------------------------------------------

(defn generic-update [id doc lookup update-fn]
  (cond
    (empty? lookup) (not-found "Object does not exist")
    :else (do (update-fn id doc)
              (response-ok "OK"))))

(defn generic-delete [id lookup delete-fn]
  (cond
    (empty? lookup) (not-found "Object does not exist")
    :else (do (delete-fn id)
              (response-ok "OK"))))

;; -------------------------------------------------------

(defn get-all-ratings []
  (response-ok {:ratings (rating/all)}))

(defn get-rating [id]
  (response-ok {:rating (rating/find-by-id id)}))

(defn create-new-rating [doc]
  (let [existing-user (user/find-by-id (doc :user_id))
        existing-item (item/find-by-id (doc :item_id))]
    (cond
      (empty? existing-user)
      (client-error "Given User does not exist")
      (empty? existing-item)
      (client-error "Given Item does not exist")
      :else (try
              (let [validated-doc (s/validate rating/Rating doc)
                    new-rating (rating/create validated-doc)]
                (created-ok (str "/ratings/" (new-rating :_id)) new-rating))
              (catch Exception e
                (client-error (str "Problem occurred: " e))))))) ;; TODO return e as map?

(defn update-rating [id doc]
  (generic-update id doc (rating/find-by-id id)
                  (fn [id doc] (rating/update id doc))))

(defn delete-rating [id]
  (generic-delete id (rating/find-by-id id)
                  (fn [id] (rating/delete id))))

;; -------------------------------------------------------

(defn get-all-users []
  (response-ok {:users (user/all)}))

(defn get-user [id]
  (response-ok {:user (user/find-by-id id)}))

(defn get-user-ratings [id]
  (response-ok {:ratings (rating/find-by-user-id id)}))

(defn create-new-user [doc]
  (let [existing-user (user/find-by-alias (doc :alias))]    ;; Avoid duplicates by alias
    (cond
      (empty? existing-user)
      (try
        (let [validated-doc (s/validate user/User doc)
              new-user (user/create validated-doc)]
          (created-ok (str "/users/" (new-user :_id)) new-user))
        (catch Exception e
          (client-error (str "Problem occurred: " e))))     ;; TODO return e as map?
      :else (client-error "User with this alias already exists"))))

(defn update-user [id doc]
  (generic-update id doc (user/find-by-id id)
                  (fn [id doc] (user/update id doc))))

(defn delete-user [id]
  (generic-delete id (user/find-by-id id)
                  (fn [id] (user/delete id))))

(defn get-similar-users
  "Looks up similar users via mahout recommendation"
  [user-id]
  (response-ok {:recommended (recommender/find-similar-users 3 user-id)}))

;; -------------------------------------------------------

(defn get-all-items []
  (response-ok {:items (item/all)}))

(defn get-item [id]
  (response-ok {:item (item/find-by-id id)}))

(defn create-new-item [doc]
  (let [existing-item (item/find-by-name (doc :name))]      ;; Avoid duplicates by name
    (cond
      (empty? existing-item)
      (try
        (let [validated-doc (s/validate item/Item doc)
              new-item (item/create validated-doc)]
          (created-ok (str "/items/" (new-item :_id)) new-item))
        (catch Exception e
          (client-error (str "Problem occurred: " e))))     ;; TODO return e as map?
      :else (client-error "Item with this name already exists"))))

(defn update-item [id doc]
  (generic-update id doc (item/find-by-id id)
                  (fn [id doc] (item/update id doc))))

(defn delete-item [id]
  (generic-delete id (item/find-by-id id)
                  (fn [id] (item/delete id))))

;; -------------------------------------------------------

(defn rnd-init
  "Generates n users with 0..k interests from a set of j possible."
  [n k j]
  (let [interests (range j)]
    (doall (for [user-id (range n)
                 user-interest (take (rand-int k) (shuffle interests))]

             (println (str user-id "," user-interest "," (rand))))))
  (println (str n "," k))
  (response-ok "OK"))

;; -------------------------------------------------------

(defroutes app-routes

           (GET "/" [] (str "Welcome to matchbox"))
           (GET "/rndinit" [] (rnd-init 50 8 20))

           (context "/users" []
                    (defroutes users-routes
                               (GET "/" [] (get-all-users))
                               (POST "/" {body :body} (create-new-user body))
                               (context "/:id" [id] (defroutes user-routes
                                                               (GET "/ratings" [] (get-user-ratings id))
                                                               (GET "/similar-users" [] (get-similar-users id))
                                                               (GET "/" [] (get-user id))
                                                               (PUT "/" {body :body} (update-user id body))
                                                               (DELETE "/" [] (delete-user id))))))

           (context "/items" []
                    (defroutes items-routes
                               (GET "/" [] (get-all-items))
                               (POST "/" {body :body} (create-new-item body))
                               (context "/:id" [id] (defroutes item-routes
                                                               (GET "/" [] (get-item id))
                                                               (PUT "/" {body :body} (update-item id body))
                                                               (DELETE "/" [] (delete-item id))))))
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
