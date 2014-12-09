# matchbox

A Clojure project to kick the tires on [Apache Mahout](http://mahout.apache.org/).

Start compojure with ring call:

    lein ring server-headless

Example call:

    (use 'matchbox.core)
    (find-similar-users 7 3)


## Requirements

MongoDB server

Under Mac OSX install by using `brew install mongo` and then start with the help of:

    sudo mongod --config /usr/local/etc/mongod.conf


## API Ideas

    POST /tastes -> neue Vorliebe anlegen 
    GET /tastes -> liefert alle vorhandenen Vorlieben zurück

    POST /users   -> neuen User anlegen
    GET /users/<userid> -> liefert User zurück, inkl. seiner Vorlieben
    POST /users/<userid>/tastes  -> hinzufügen einer neuen Vorliebe
    DELETE /users/<userid>/tastes/<taste-id> -> löscht eine Vorliebe

    GET /users/<userid>/similar-users  -> liefert eine Liste mit Usern, die ähnliche Vorlieben haben


JSON für taste:

    {
      "id": "vom-Server-vergeben",
      "name": {
        "en": "Cinema films",
        "de": "Kinofilme",
        "ru": "фильмкинолента"
      }
    }