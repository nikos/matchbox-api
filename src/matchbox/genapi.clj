(ns matchbox.genapi
  (:use compojure.core)
  (:use cheshire.core)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [monger.core :as mg]
            [monger.json]
            [monger.collection :as mc]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]))

(def get-db
  (mg/get-db (mg/connect) "monger-test"))


(defn get-all-documents []
  (response {:results (mc/find-maps get-db "documents")}))

(defn create-new-document [doc]
  (let [document (mc/find-maps get-db "documents" {:title (doc "title")})]
    (cond
      (empty? document) (response (mc/insert-and-return get-db "documents" doc))
      :else (response {:result (doc "title")}))))

(defroutes app-routes
           (context "/documents" []
                    (defroutes documents-routes
                               (GET "/" [] (get-all-documents))
                               (POST "/" {body :body} (create-new-document body))))
           (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))