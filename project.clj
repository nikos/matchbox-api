(defproject matchbox "0.2.0-SNAPSHOT"
  :description "RESTful Recommendation API (built on top of Mahout)"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :license {:name "Apache License"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}

  :dependencies [[org.clojure/clojure "1.6.0"]

                 ;; recommendation engine
                 [org.apache.mahout/mahout-core "0.9" :exclusions [commons-codec]]
                 [org.apache.mahout/mahout-integration "0.9"]

                 ;; mongo persistence
                 [com.novemberain/monger "2.0.1"]

                 ;; Natural Language Processing
                 [clojure-opennlp "0.3.3"]  ;; uses Opennlp 1.5.3
                 [clojure-stemmer "0.1.0"]
                 [cheshire "5.3.1"]

                 ;; schema validation
                 [prismatic/schema "0.3.3"]

                 ;; web stuff
                 [compojure "1.3.1"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-defaults "0.1.3"]
                 [ring.middleware.logger "0.5.0"]]

  :plugins [[lein-ring "0.9.0"]]

  :ring {:handler matchbox.handler/app
         :port 3001}

  ;; Only required for debugging
  ;; :jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"]

  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [midje "1.6.3"]
                        [ring-mock "0.1.5"]]}})

  ;;:profiles {:dev        {:jvm-opts ["-Dproperty-file=dev.properties"]
  ;;                        :ring {:port 8080}}
  ;;           :ci         {:jvm-opts ["-Dproperty-file=ci.properties"]
  ;;                        :ring {:port 80}}
  ;;           :uberjar    {:aot :all
  ;;                        :ring {:port 80}}})