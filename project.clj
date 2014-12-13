(defproject matchbox "0.1.2-SNAPSHOT"
  :description "RESTful Recommendation API (built on top of Mahout)"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :license {:name "Apache License"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.apache.mahout/mahout-core "0.9" :exclusions [commons-codec]]
                 [org.apache.mahout/mahout-integration "0.9"]
                 [com.novemberain/monger "2.0.1"]
                 [compojure "1.3.1"]
                 [ring.middleware.logger "0.5.0"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-json "0.3.1"]]
  :plugins [[lein-ring "0.8.13"]]

  :ring {:handler matchbox.handler/app}

  ;; Only required for debugging
  ;; :jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"]

  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
