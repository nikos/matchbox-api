(ns matchbox.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [matchbox.core :as mc]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]))


(defn my-calc
  "Just testing"
  []
  (* 5 25))


(defn get-similar-users
  "Looks up similar user via mahout"
  [user-id]
  ;;(str "sim" user-id)
  {:recommended (mc/find-similar-users 3 user-id)}
  )


(defroutes app-routes
           (GET "/" []
                (str "Huhu World" (my-calc)))
           (GET "/test.json" []
                (response {:nickname "getMessages" :summary "Get message"}))
           (GET "/sim/:user-id" [user-id]
                (response (get-similar-users (Integer/parseInt user-id))))
           (route/not-found
                "Not Found"))

;; TODO: Add logging: https://github.com/pjlegato/ring.middleware.logger
(def app
  (-> (handler/api app-routes)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))

;;  (-> handler
;;    (wrap wrap-flash (get-in config [:session :flash] false))


;; (-> #'handler
;;    (ring.middleware.stacktrace/wrap-stacktrace)

;;    (wrap-defaults app-routes site-defaults)
