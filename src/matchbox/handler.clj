(ns matchbox.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [matchbox.core :as mc]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn my-calc
  "Just testing"
  []
  (* 5 25))


(defn show-similar-users
  "Looks up similar user via mahout"
  [user-id]
  ;;(str "sim" user-id)
  (str "Result:" (mc/find-similar-users 3 user-id))
  ;; (with-connection config/db-connection-params
  ;;                 (view/edit (model/find-id id)))
  )


(defroutes app-routes
  (GET "/" [] (str "Huhu World" (my-calc)))
  (GET "/sim/:user-id" [user-id] (show-similar-users (Integer/parseInt user-id)))
  (route/not-found "Not Found"))

;; TODO: Add logging: https://github.com/pjlegato/ring.middleware.logger
(def app
  (wrap-defaults app-routes site-defaults))
