(ns matchbox.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [matchbox.core :as mc]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn my-calc
  "Just testing"
  []
  (* 5 25))


(defn show-similar-users [user-id]
  ;;(str "sim" user-id)
  (str "Result:" (mc/find-similar-users 3 0))
  ;; (with-connection config/db-connection-params
  ;;                 (view/edit (model/find-id id)))
  )


(defroutes app-routes
  (GET "/" [] (str "Huhu World" (my-calc)))
  (GET "/sim/:user-id" [user-id] (show-similar-users user-id))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
