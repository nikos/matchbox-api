(ns matchbox.handler
  (:import (clojure.lang ExceptionInfo))
  (:use compojure.core)
  (:use cheshire.core)
  (:use ring.util.response)
  (:require [matchbox.services.recommender :as recommender]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [monger.json]
            [matchbox.models.user :as user]
            [matchbox.models.item :as item]
            [matchbox.models.sentiment :as sentiment]
            [matchbox.models.rating :as rating]
            [sentimental.core :as sent]
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
   :headers {"Access-Control-Allow-Origin" "*"
             "Access-Control-Request-Methods" "GET,POST,PUT"}
   :body    body})

(defn created-ok
  "Returns a Ring response for a HTTP 201 created response."
  {:added "1.2"}
  ([url] (created-ok url nil))
  ([url body]
    {:status  201
     :headers {"Location"                    url
               "Access-Control-Allow-Origin" "*"
               "Access-Control-Request-Methods" "GET,POST,PUT"}
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

;; TODO: Limit to the newest N sentiments (aka sentiment feed)
(defn get-all-sentiments []
  (response-ok {:sentiments (sentiment/all)}))

(defn get-sentiment [id]
  (response-ok {:sentiment (sentiment/find-by-id id)}))

(defn prepare-sentiment
  [params body]
  (let [user-id (or (:user_id params) (:user_id body))
        sentence (or (:sentence params) (:sentence body))]
    {:user_id user-id :sentence sentence}))

(defn analyze-sentiment
  "Analyzes sentiment of a given sentence to allow easier testing quality"
  [raw-sentiment]
  (let [sentence (:sentence raw-sentiment)
        tok-sentence (sent/pos-tag (sent/tokenize sentence))
        nouns (sent/extract-single-and-double-nouns tok-sentence)
        negatives (sent/grab-negative-tuples tok-sentence)
        category (sent/categorize sentence negatives)]
    (response-ok {:sentence sentence
                  :tokens tok-sentence
                  :nouns nouns
                  :category category})))

(defn create-new-sentiment
  "Adds new sentiment in the database (incl. validation), resolves user by their given ID"
  [raw-sentiment]
  (let [existing-user (user/find-by-id (raw-sentiment :user_id))]
    (cond
      (empty? existing-user)
      (client-error "Given User does not exist")
      :else (try
              (let [sentiment (assoc raw-sentiment :user existing-user)
                    validated-sentiment (s/validate sentiment/NewSentiment sentiment)
                    new-sentiment (sentiment/create validated-sentiment)]
                (created-ok (str "/sentiments/" (new-sentiment :_id)) new-sentiment))
              (catch Exception e
                (client-error (str "Problem occurred: " e))))))) ;; TODO return e as map?

(defn delete-sentiment [id]
  (generic-delete id (sentiment/find-by-id id)
                  (fn [id] (sentiment/delete id))))

;; -------------------------------------------------------

;; TODO: Limit to the newest N ratings (aka rating feed)
(defn get-all-ratings []
  (response-ok {:ratings (rating/all)}))

(defn get-rating [id]
  (response-ok {:rating (rating/find-by-id id)}))

(defn prepare-rating
  [params body]
  (let [user-id (or (:user_id params) (:user_id body))
        item-id (or (:item_id params) (:item_id body))
        pref-double (or (Double/parseDouble (:preference params)) (:preference body))]
    {:user_id user-id :item_id item-id :preference pref-double}))

(defn create-new-rating
  "Adds new rating in the database (incl. validation), resolves user and item by their given IDs"
  [raw-rating]
  (let [existing-user (user/find-by-id (raw-rating :user_id))
        existing-item (item/find-by-id (raw-rating :item_id))]
    (cond
      (empty? existing-user)
      (client-error "Given User does not exist")
      (empty? existing-item)
      (client-error "Given Item does not exist")
      :else (try
              (let [rating (assoc raw-rating :user existing-user :item existing-item)
                    validated-rating (s/validate rating/NewRating rating)
                    new-rating (rating/create validated-rating)]
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

(defn prepare-user
  [params body]
  (let [user-short (or (:alias params) (:alias body))
        user-first (or (:first_name params) (:first_name body))
        user-last (or (:last_name params) (:last_name body))]
    {:alias user-short :first_name user-first :last_name user-last}))

(defn create-new-user
  "Creates new user in the database (incl. validation)"
  [raw-user]
  (let [existing-user (user/find-by-alias (raw-user :alias))]    ;; Avoid duplicates by alias
    (cond
      (empty? existing-user)
      (try
        (let [validated-user (s/validate user/NewUser raw-user)
              new-user (user/create validated-user)]
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
  (let [item (item/find-by-id id)]
    (cond
      (empty? item) (not-found "Item does not exist")
      :else (response-ok {:item item}))))

(defn prepare-item
  [params body]
  (let [name (or (:name params) (:name body))]
    {:name name}))

(defn create-new-item
  "Creates new item in the database (incl. validation)"
  [raw-item]
  (let [existing-item (item/find-by-name (raw-item :name))]      ;; Avoid duplicates by name
    (cond
      (empty? existing-item)
      (try
        (let [validated-item (s/validate item/NewItem raw-item)
              new-item (item/create validated-item)]
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
                               (POST "/" {params :params body :body} (create-new-user (prepare-user params body)))
                               (context "/:id" [id] (defroutes user-routes
                                                               (GET "/ratings" [] (get-user-ratings id))
                                                               (GET "/similar-users" [] (get-similar-users id))
                                                               (GET "/" [] (get-user id))
                                                               (PUT "/" {body :body} (update-user id body))
                                                               (DELETE "/" [] (delete-user id))))))
           (context "/items" []
                    (defroutes items-routes
                               (GET "/" [] (get-all-items))
                               (POST "/" {params :params body :body} (create-new-item (prepare-item params body)))
                               (context "/:id" [id] (defroutes item-routes
                                                               (GET "/" [] (get-item id))
                                                               (PUT "/" {body :body} (update-item id body))
                                                               (DELETE "/" [] (delete-item id))))))
           (context "/ratings" []
                    (defroutes ratings-routes
                               (GET "/" [] (get-all-ratings))
                               (POST "/" {params :params body :body} (create-new-rating (prepare-rating params body)))
                               (context "/:id" [id] (defroutes rating-routes
                                                               (GET "/" [] (get-rating id))
                                                               (PUT "/" {body :body} (update-rating id body))
                                                               (DELETE "/" [] (delete-rating id))))))
           (context "/sentiments" []
                    (defroutes sentiments-routes
                               (GET "/" [] (get-all-sentiments))
                               (POST "/analyze" {params :params body :body} (analyze-sentiment (prepare-sentiment params body)))
                               (POST "/" {params :params body :body} (create-new-sentiment (prepare-sentiment params body)))
                               (context "/:id" [id] (defroutes sentiment-routes
                                                               (GET "/" [] (get-sentiment id))
                                                               (DELETE "/" [] (delete-sentiment id))))))
           (route/not-found "Not Found"))


;; Bootstraps the API (refer by project.clj)
(def app
  (-> (handler/api app-routes)                              ;; TODO: doch lieber wieder zurück zu defaults? (01-12)
      (wrap-with-logger)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))
