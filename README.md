# matchbox

A Clojure project to kick the tires on [Apache Mahout](http://mahout.apache.org/).

Start compojure with ring call:

    lein ring server-headless

Example call:

    (use 'matchbox.services.recommender)
    (find-similar-users 7 3)


## Requirements

MongoDB server

Under Mac OSX install by using `brew install mongo` and then start with the help of:

    sudo mongod --config /usr/local/etc/mongod.conf


## Manage items

http -v POST http://nava.de:3000/items/ name=Billard
POST /items/ HTTP/1.1
Accept: application/json
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 19
Content-Type: application/json; charset=utf-8
Host: nava.de:3000
User-Agent: HTTPie/0.8.0

{
    "name": "Billard"
}

HTTP/1.1 200 OK
Content-Length: 51
Content-Type: application/json; charset=utf-8
Date: Sun, 14 Dec 2014 00:44:20 GMT
Server: Jetty(7.6.8.v20121106)

{
    "_id": "548cdd64d282a2a47b2492ae",
    "name": "Billard"
}

    http http://nava.de:3000/items/

Leads to the following sample result:

    HTTP/1.1 200 OK
    Content-Length: 317
    Content-Type: application/json; charset=utf-8
    Date: Sun, 14 Dec 2014 00:44:29 GMT
    Server: Jetty(7.6.8.v20121106)

    {
        "items": [
            {
                "_id": "548cdd3ed282a2a47b2492a9",
                "name": "Kino"
            },
            {
                "_id": "548cdd54d282a2a47b2492ab",
                "name": "Boxen"
            },
            {
                "_id": "548cdd64d282a2a47b2492ae",
                "name": "Billard"
            }
        ]
    }


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