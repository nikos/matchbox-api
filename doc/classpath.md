
lein deps :tree
Possibly confusing dependencies found:
[lein-ring "0.8.13"] -> [org.clojure/clojure "1.2.1"]
 overrides
[lein-ring "0.8.13"] -> [leinjacker "0.4.1"] -> [org.clojure/core.contracts "0.0.1"] -> [org.clojure/core.unify "0.5.3"] -> [org.clojure/clojure "1.4.0"]
 and
[lein-ring "0.8.13"] -> [leinjacker "0.4.1"] -> [org.clojure/core.contracts "0.0.1"] -> [org.clojure/clojure "1.4.0"]
 and
[lein-ring "0.8.13"] -> [org.clojure/data.xml "0.0.6"] -> [org.clojure/clojure "1.3.0"]

Consider using these exclusions:
[lein-ring "0.8.13" :exclusions [org.clojure/clojure]]
[lein-ring "0.8.13" :exclusions [org.clojure/clojure]]
[lein-ring "0.8.13" :exclusions [org.clojure/clojure]]

Possibly confusing dependencies found:
[org.apache.mahout/mahout-core "0.9"] -> [org.apache.hadoop/hadoop-core "1.2.1" :exclusions [net.sf.kosmosfs/kfs org.mortbay.jetty/jetty org.mortbay.jetty/jetty-util hsqldb junit oro org.mortbay.jetty/jsp-2.1 org.mortbay.jetty/jsp-api-2.1 org.mortbay.jetty/servlet-api-2.5 tomcat/jasper-runtime tomcat/jasper-compiler xmlenc net.java.dev.jets3t/jets3t org.eclipse.jdt/core]] -> [commons-codec "1.4"]
 overrides
[ring/ring-defaults "0.1.2"] -> [ring/ring-headers "0.1.1"] -> [ring/ring-core "1.3.0"] -> [crypto-random "1.2.0"] -> [commons-codec "1.6"]
 and
[ring/ring-defaults "0.1.2"] -> [ring/ring-headers "0.1.1"] -> [ring/ring-core "1.3.0"] -> [ring/ring-codec "1.0.0"] -> [commons-codec "1.6"]
 and
[ring/ring-defaults "0.1.2"] -> [ring/ring-ssl "0.2.1"] -> [ring/ring-core "1.3.0-RC1"] -> [crypto-random "1.2.0"] -> [commons-codec "1.6"]
 and
[ring/ring-defaults "0.1.2"] -> [ring/ring-ssl "0.2.1"] -> [ring/ring-core "1.3.0-RC1"] -> [ring/ring-codec "1.0.0"] -> [commons-codec "1.6"]
 and
[ring/ring-defaults "0.1.2"] -> [ring/ring-anti-forgery "1.0.0"] -> [crypto-random "1.2.0"] -> [commons-codec "1.6"]
 and
[ring/ring-defaults "0.1.2"] -> [ring/ring-core "1.3.1"] -> [crypto-random "1.2.0"] -> [commons-codec "1.6"]
 and
[ring/ring-defaults "0.1.2"] -> [ring/ring-core "1.3.1"] -> [ring/ring-codec "1.0.0"] -> [commons-codec "1.6"]
 and
[compojure "1.2.0"] -> [ring/ring-core "1.3.1"] -> [crypto-random "1.2.0"] -> [commons-codec "1.6"]
 and
[compojure "1.2.0"] -> [ring/ring-core "1.3.1"] -> [ring/ring-codec "1.0.0"] -> [commons-codec "1.6"]
 and
[ring-mock "0.1.5"] -> [ring/ring-codec "1.0.0"] -> [commons-codec "1.6"]
 and
[compojure "1.2.0"] -> [ring/ring-codec "1.0.0"] -> [commons-codec "1.6"]

Consider using these exclusions:
[ring/ring-defaults "0.1.2" :exclusions [commons-codec]]
[ring/ring-defaults "0.1.2" :exclusions [commons-codec]]
[ring/ring-defaults "0.1.2" :exclusions [commons-codec]]
[ring/ring-defaults "0.1.2" :exclusions [commons-codec]]
[ring/ring-defaults "0.1.2" :exclusions [commons-codec]]
[ring/ring-defaults "0.1.2" :exclusions [commons-codec]]
[ring/ring-defaults "0.1.2" :exclusions [commons-codec]]
[compojure "1.2.0" :exclusions [commons-codec]]
[compojure "1.2.0" :exclusions [commons-codec]]
[ring-mock "0.1.5" :exclusions [commons-codec]]
[compojure "1.2.0" :exclusions [commons-codec]]

[org.apache.mahout/mahout-core "0.9"] -> [org.apache.hadoop/hadoop-core "1.2.1" :exclusions [net.sf.kosmosfs/kfs org.mortbay.jetty/jetty org.mortbay.jetty/jetty-util hsqldb junit oro org.mortbay.jetty/jsp-2.1 org.mortbay.jetty/jsp-api-2.1 org.mortbay.jetty/servlet-api-2.5 tomcat/jasper-runtime tomcat/jasper-compiler xmlenc net.java.dev.jets3t/jets3t org.eclipse.jdt/core]] -> [commons-httpclient "3.0.1"] -> [commons-logging "1.0.3"]
 overrides
[org.apache.mahout/mahout-integration "0.9"] -> [org.apache.mahout/mahout-core "0.9"] -> [org.apache.hadoop/hadoop-core "1.2.1" :exclusions [net.sf.kosmosfs/kfs org.mortbay.jetty/jetty org.mortbay.jetty/jetty-util hsqldb junit oro org.mortbay.jetty/jsp-2.1 org.mortbay.jetty/jsp-api-2.1 org.mortbay.jetty/servlet-api-2.5 tomcat/jasper-runtime tomcat/jasper-compiler xmlenc net.java.dev.jets3t/jets3t org.eclipse.jdt/core]] -> [commons-configuration "1.6"] -> [commons-beanutils/commons-beanutils-core "1.8.0"] -> [commons-logging "1.1.1"]
 and
[org.apache.mahout/mahout-integration "0.9"] -> [org.apache.mahout/mahout-core "0.9"] -> [org.apache.hadoop/hadoop-core "1.2.1" :exclusions [net.sf.kosmosfs/kfs org.mortbay.jetty/jetty org.mortbay.jetty/jetty-util hsqldb junit oro org.mortbay.jetty/jsp-2.1 org.mortbay.jetty/jsp-api-2.1 org.mortbay.jetty/servlet-api-2.5 tomcat/jasper-runtime tomcat/jasper-compiler xmlenc net.java.dev.jets3t/jets3t org.eclipse.jdt/core]] -> [commons-configuration "1.6"] -> [commons-digester "1.8"] -> [commons-logging "1.1"]
 and
[org.apache.mahout/mahout-integration "0.9"] -> [org.apache.mahout/mahout-core "0.9"] -> [org.apache.hadoop/hadoop-core "1.2.1" :exclusions [net.sf.kosmosfs/kfs org.mortbay.jetty/jetty org.mortbay.jetty/jetty-util hsqldb junit oro org.mortbay.jetty/jsp-2.1 org.mortbay.jetty/jsp-api-2.1 org.mortbay.jetty/servlet-api-2.5 tomcat/jasper-runtime tomcat/jasper-compiler xmlenc net.java.dev.jets3t/jets3t org.eclipse.jdt/core]] -> [commons-configuration "1.6"] -> [commons-logging "1.1.1"]
 and
[org.apache.mahout/mahout-core "0.9"] -> [org.apache.hadoop/hadoop-core "1.2.1" :exclusions [net.sf.kosmosfs/kfs org.mortbay.jetty/jetty org.mortbay.jetty/jetty-util hsqldb junit oro org.mortbay.jetty/jsp-2.1 org.mortbay.jetty/jsp-api-2.1 org.mortbay.jetty/servlet-api-2.5 tomcat/jasper-runtime tomcat/jasper-compiler xmlenc net.java.dev.jets3t/jets3t org.eclipse.jdt/core]] -> [commons-configuration "1.6"] -> [commons-beanutils/commons-beanutils-core "1.8.0"] -> [commons-logging "1.1.1"]
 and
[org.apache.mahout/mahout-core "0.9"] -> [org.apache.hadoop/hadoop-core "1.2.1" :exclusions [net.sf.kosmosfs/kfs org.mortbay.jetty/jetty org.mortbay.jetty/jetty-util hsqldb junit oro org.mortbay.jetty/jsp-2.1 org.mortbay.jetty/jsp-api-2.1 org.mortbay.jetty/servlet-api-2.5 tomcat/jasper-runtime tomcat/jasper-compiler xmlenc net.java.dev.jets3t/jets3t org.eclipse.jdt/core]] -> [commons-configuration "1.6"] -> [commons-digester "1.8"] -> [commons-logging "1.1"]
 and
[org.apache.mahout/mahout-core "0.9"] -> [org.apache.hadoop/hadoop-core "1.2.1" :exclusions [net.sf.kosmosfs/kfs org.mortbay.jetty/jetty org.mortbay.jetty/jetty-util hsqldb junit oro org.mortbay.jetty/jsp-2.1 org.mortbay.jetty/jsp-api-2.1 org.mortbay.jetty/servlet-api-2.5 tomcat/jasper-runtime tomcat/jasper-compiler xmlenc net.java.dev.jets3t/jets3t org.eclipse.jdt/core]] -> [commons-configuration "1.6"] -> [commons-logging "1.1.1"]

Consider using these exclusions:
[org.apache.mahout/mahout-integration "0.9" :exclusions [commons-logging]]
[org.apache.mahout/mahout-integration "0.9" :exclusions [commons-logging]]
[org.apache.mahout/mahout-integration "0.9" :exclusions [commons-logging]]
[org.apache.mahout/mahout-core "0.9" :exclusions [commons-logging]]
[org.apache.mahout/mahout-core "0.9" :exclusions [commons-logging]]
[org.apache.mahout/mahout-core "0.9" :exclusions [commons-logging]]

warn
 [clojure-complete "0.2.3" :scope "test" :exclusions [[org.clojure/clojure]]]
 [compojure "1.2.0"]
   [clout "2.0.0"]
     [instaparse "1.3.4" :exclusions [[org.clojure/clojure]]]
   [medley "0.5.1"]
   [org.clojure/tools.macro "0.1.5"]
   [ring/ring-codec "1.0.0"]
   [ring/ring-core "1.3.1"]
     [clj-time "0.6.0"]
       [joda-time "2.2"]
     [commons-fileupload "1.3"]
     [crypto-equality "1.0.0"]
     [crypto-random "1.2.0"]
     [org.clojure/tools.reader "0.8.1"]
 [javax.servlet/servlet-api "2.5" :scope "test"]
 [mysql/mysql-connector-java "5.1.34"]
 [org.apache.mahout/mahout-core "0.9"]
   [com.thoughtworks.xstream/xstream "1.4.4"]
     [xmlpull "1.1.3.1"]
     [xpp3/xpp3_min "1.1.4c"]
   [org.apache.commons/commons-lang3 "3.1"]
   [org.apache.commons/commons-math3 "3.2"]
   [org.apache.hadoop/hadoop-core "1.2.1" :exclusions [[net.sf.kosmosfs/kfs] [org.mortbay.jetty/jetty] [org.mortbay.jetty/jetty-util] [hsqldb] [junit] [oro] [org.mortbay.jetty/jsp-2.1] [org.mortbay.jetty/jsp-api-2.1] [org.mortbay.jetty/servlet-api-2.5] [tomcat/jasper-runtime] [tomcat/jasper-compiler] [xmlenc] [net.java.dev.jets3t/jets3t] [org.eclipse.jdt/core]]]
     [com.sun.jersey/jersey-core "1.8"]
     [com.sun.jersey/jersey-json "1.8"]
       [com.sun.xml.bind/jaxb-impl "2.2.3-1"]
         [javax.xml.bind/jaxb-api "2.2.2"]
           [javax.activation/activation "1.1"]
           [javax.xml.stream/stax-api "1.0-2"]
       [org.codehaus.jackson/jackson-jaxrs "1.7.1"]
       [org.codehaus.jackson/jackson-xc "1.7.1"]
       [org.codehaus.jettison/jettison "1.1"]
         [stax/stax-api "1.0.1"]
     [com.sun.jersey/jersey-server "1.8"]
       [asm "3.1"]
     [commons-cli "1.2"]
     [commons-codec "1.4"]
     [commons-configuration "1.6"]
       [commons-beanutils/commons-beanutils-core "1.8.0"]
       [commons-collections "3.2.1"]
       [commons-digester "1.8"]
         [commons-beanutils "1.7.0"]
       [commons-lang "2.4"]
     [commons-el "1.0"]
     [commons-httpclient "3.0.1"]
       [commons-logging "1.0.3"]
     [commons-net "1.4.1"]
     [org.apache.commons/commons-math "2.1"]
   [org.apache.lucene/lucene-analyzers-common "4.6.1"]
   [org.apache.lucene/lucene-core "4.6.1"]
   [org.apache.mahout.commons/commons-cli "2.0-mahout"]
   [org.apache.mahout/mahout-math "0.9"]
     [com.google.guava/guava "16.0"]
   [org.apache.solr/solr-commons-csv "3.5.0"]
   [org.codehaus.jackson/jackson-core-asl "1.9.12"]
   [org.codehaus.jackson/jackson-mapper-asl "1.9.12"]
   [org.slf4j/slf4j-api "1.7.5"]
 [org.apache.mahout/mahout-integration "0.9"]
   [commons-io "2.4"]
 [org.clojure/clojure "1.6.0"]
 [org.clojure/tools.nrepl "0.2.6" :scope "test" :exclusions [[org.clojure/clojure]]]
 [ring-mock "0.1.5" :scope "test"]
 [ring/ring-defaults "0.1.2"]
   [ring/ring-anti-forgery "1.0.0"]
     [hiccup "1.0.5"]
   [ring/ring-headers "0.1.1"]
   [ring/ring-ssl "0.2.1"]


==============
lein classpath

lein repl

user=> (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader)))

(#<URL file:/Users/niko/sandbox/clojure/matchbox/test/> #<URL file:/Users/niko/sandbox/clojure/matchbox/src/> #<URL file:/Users/niko/sandbox/clojure/matchbox/dev-resources> #<URL file:/Users/niko/sandbox/clojure/matchbox/resources> #<URL file:/Users/niko/sandbox/clojure/matchbox/target/classes/> #<URL file:/Users/niko/.m2/repository/commons-beanutils/commons-beanutils-core/1.8.0/commons-beanutils-core-1.8.0.jar> #<URL file:/Users/niko/.m2/repository/org/codehaus/jackson/jackson-jaxrs/1.7.1/jackson-jaxrs-1.7.1.jar> #<URL file:/Users/niko/.m2/repository/commons-net/commons-net/1.4.1/commons-net-1.4.1.jar> #<URL file:/Users/niko/.m2/repository/org/apache/mahout/commons/commons-cli/2.0-mahout/commons-cli-2.0-mahout.jar> #<URL file:/Users/niko/.m2/repository/org/apache/lucene/lucene-analyzers-common/4.6.1/lucene-analyzers-common-4.6.1.jar> #<URL file:/Users/niko/.m2/repository/mysql/mysql-connector-java/5.1.34/mysql-connector-java-5.1.34.jar> #<URL file:/Users/niko/.m2/repository/asm/asm/3.1/asm-3.1.jar> #<URL file:/Users/niko/.m2/repository/commons-codec/commons-codec/1.4/commons-codec-1.4.jar> #<URL file:/Users/niko/.m2/repository/commons-configuration/commons-configuration/1.6/commons-configuration-1.6.jar> #<URL file:/Users/niko/.m2/repository/org/clojure/tools.macro/0.1.5/tools.macro-0.1.5.jar> #<URL file:/Users/niko/.m2/repository/ring/ring-headers/0.1.1/ring-headers-0.1.1.jar> #<URL file:/Users/niko/.m2/repository/ring/ring-ssl/0.2.1/ring-ssl-0.2.1.jar> #<URL file:/Users/niko/.m2/repository/clojure-complete/clojure-complete/0.2.3/clojure-complete-0.2.3.jar> #<URL file:/Users/niko/.m2/repository/stax/stax-api/1.0.1/stax-api-1.0.1.jar> #<URL file:/Users/niko/.m2/repository/ring/ring-codec/1.0.0/ring-codec-1.0.0.jar> #<URL file:/Users/niko/.m2/repository/joda-time/joda-time/2.2/joda-time-2.2.jar> #<URL file:/Users/niko/.m2/repository/org/codehaus/jettison/jettison/1.1/jettison-1.1.jar> #<URL file:/Users/niko/.m2/repository/org/codehaus/jackson/jackson-mapper-asl/1.9.12/jackson-mapper-asl-1.9.12.jar> #<URL file:/Users/niko/.m2/repository/javax/activation/activation/1.1/activation-1.1.jar> #<URL file:/Users/niko/.m2/repository/commons-beanutils/commons-beanutils/1.7.0/commons-beanutils-1.7.0.jar> #<URL file:/Users/niko/.m2/repository/org/slf4j/slf4j-api/1.7.5/slf4j-api-1.7.5.jar> #<URL file:/Users/niko/.m2/repository/ring/ring-defaults/0.1.2/ring-defaults-0.1.2.jar> #<URL file:/Users/niko/.m2/repository/javax/xml/bind/jaxb-api/2.2.2/jaxb-api-2.2.2.jar> #<URL file:/Users/niko/.m2/repository/medley/medley/0.5.1/medley-0.5.1.jar> #<URL file:/Users/niko/.m2/repository/org/apache/mahout/mahout-core/0.9/mahout-core-0.9.jar> #<URL file:/Users/niko/.m2/repository/xmlpull/xmlpull/1.1.3.1/xmlpull-1.1.3.1.jar> #<URL file:/Users/niko/.m2/repository/org/apache/solr/solr-commons-csv/3.5.0/solr-commons-csv-3.5.0.jar> #<URL file:/Users/niko/.m2/repository/com/google/guava/guava/16.0/guava-16.0.jar> #<URL file:/Users/niko/.m2/repository/commons-httpclient/commons-httpclient/3.0.1/commons-httpclient-3.0.1.jar> #<URL file:/Users/niko/.m2/repository/commons-io/commons-io/2.4/commons-io-2.4.jar> #<URL file:/Users/niko/.m2/repository/crypto-equality/crypto-equality/1.0.0/crypto-equality-1.0.0.jar> #<URL file:/Users/niko/.m2/repository/com/thoughtworks/xstream/xstream/1.4.4/xstream-1.4.4.jar> #<URL file:/Users/niko/.m2/repository/ring-mock/ring-mock/0.1.5/ring-mock-0.1.5.jar> #<URL file:/Users/niko/.m2/repository/commons-collections/commons-collections/3.2.1/commons-collections-3.2.1.jar> #<URL file:/Users/niko/.m2/repository/instaparse/instaparse/1.3.4/instaparse-1.3.4.jar> #<URL file:/Users/niko/.m2/repository/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1.jar> #<URL file:/Users/niko/.m2/repository/commons-el/commons-el/1.0/commons-el-1.0.jar> #<URL file:/Users/niko/.m2/repository/com/sun/jersey/jersey-core/1.8/jersey-core-1.8.jar> #<URL file:/Users/niko/.m2/repository/org/apache/commons/commons-math/2.1/commons-math-2.1.jar> #<URL file:/Users/niko/.m2/repository/clj-time/clj-time/0.6.0/clj-time-0.6.0.jar> #<URL file:/Users/niko/.m2/repository/commons-fileupload/commons-fileupload/1.3/commons-fileupload-1.3.jar> #<URL file:/Users/niko/.m2/repository/hiccup/hiccup/1.0.5/hiccup-1.0.5.jar> #<URL file:/Users/niko/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar> #<URL file:/Users/niko/.m2/repository/ring/ring-core/1.3.1/ring-core-1.3.1.jar> #<URL file:/Users/niko/.m2/repository/compojure/compojure/1.2.0/compojure-1.2.0.jar> #<URL file:/Users/niko/.m2/repository/org/codehaus/jackson/jackson-core-asl/1.9.12/jackson-core-asl-1.9.12.jar> #<URL file:/Users/niko/.m2/repository/org/apache/mahout/mahout-integration/0.9/mahout-integration-0.9.jar> #<URL file:/Users/niko/.m2/repository/javax/xml/stream/stax-api/1.0-2/stax-api-1.0-2.jar> #<URL file:/Users/niko/.m2/repository/com/sun/xml/bind/jaxb-impl/2.2.3-1/jaxb-impl-2.2.3-1.jar> #<URL file:/Users/niko/.m2/repository/com/sun/jersey/jersey-server/1.8/jersey-server-1.8.jar> #<URL file:/Users/niko/.m2/repository/com/sun/jersey/jersey-json/1.8/jersey-json-1.8.jar> #<URL file:/Users/niko/.m2/repository/org/apache/hadoop/hadoop-core/1.2.1/hadoop-core-1.2.1.jar> #<URL file:/Users/niko/.m2/repository/ring/ring-anti-forgery/1.0.0/ring-anti-forgery-1.0.0.jar> #<URL file:/Users/niko/.m2/repository/commons-digester/commons-digester/1.8/commons-digester-1.8.jar> #<URL file:/Users/niko/.m2/repository/org/apache/lucene/lucene-core/4.6.1/lucene-core-4.6.1.jar> #<URL file:/Users/niko/.m2/repository/clout/clout/2.0.0/clout-2.0.0.jar> #<URL file:/Users/niko/.m2/repository/org/clojure/tools.nrepl/0.2.6/tools.nrepl-0.2.6.jar> #<URL file:/Users/niko/.m2/repository/crypto-random/crypto-random/1.2.0/crypto-random-1.2.0.jar> #<URL file:/Users/niko/.m2/repository/commons-logging/commons-logging/1.0.3/commons-logging-1.0.3.jar> #<URL file:/Users/niko/.m2/repository/org/clojure/tools.reader/0.8.1/tools.reader-0.8.1.jar> #<URL file:/Users/niko/.m2/repository/org/clojure/clojure/1.6.0/clojure-1.6.0.jar> #<URL file:/Users/niko/.m2/repository/xpp3/xpp3_min/1.1.4c/xpp3_min-1.1.4c.jar> #<URL file:/Users/niko/.m2/repository/org/apache/mahout/mahout-math/0.9/mahout-math-0.9.jar> #<URL file:/Users/niko/.m2/repository/javax/servlet/servlet-api/2.5/servlet-api-2.5.jar> #<URL file:/Users/niko/.m2/repository/org/codehaus/jackson/jackson-xc/1.7.1/jackson-xc-1.7.1.jar> #<URL file:/Users/niko/.m2/repository/org/apache/commons/commons-math3/3.2/commons-math3-3.2.jar> #<URL file:/Users/niko/.m2/repository/commons-lang/commons-lang/2.4/commons-lang-2.4.jar>)