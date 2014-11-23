# matchbox

A Clojure project to kick the tires on [Apache Mahout](http://mahout.apache.org/).

Start compojure with ring call:

    lein ring server-headless

Example call:

    (use 'matchbox.core)
    (find-similar-users 7 3)