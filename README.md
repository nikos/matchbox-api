# matchbox

A Clojure project to kick the tires on [Apache Mahout](http://mahout.apache.org/).

Start compojure with ring call:

    lein ring server-headless

Example call:

    (use 'matchbox.services.recommender)
    (find-similar-users 7 3)

To start running the tests and watch for updates use:

    lein midje :autotest


## Requirements

MongoDB server

Under Mac OSX install by using `brew install mongo` and then start with the help of:

    sudo mongod --config /usr/local/etc/mongod.conf


## Manage items

    http POST http://matchbox.nava.de:3000/items/ name=Billard

    http GET http://matchbox.nava.de:3000/items/

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

## Manage users

Create a new user

    http POST http://matchbox.nava.de:3000/users/ alias=rocko first_name=Rocko  last_name=Schamoni

Get all users:

    http GET http://matchbox.nava.de:3000/users

    {
        "users": [
            {
                "_id": "548dbf1bd2829fb7bf535bbf",
                "alias": "heinz",
                "first_name": "heinz",
                "last_name": "Strunk"
            },
            {
                "_id": "548dbf49d2829fb7bf535bc0",
                "alias": "rocko",
                "first_name": "Rocko",
                "last_name": "Schamoni"
            },
            {
                "_id": "548dbf5fd2829fb7bf535bc1",
                "alias": "jac",
                "first_name": "Jacques",
                "last_name": "Palminger"
            }
        ]
    }

## Ratings

Add a new rating:

    http POST http://matchbox.nava.de:3000/ratings/ item_id=548cdd3ed282a2a47b2492a9 user_id=548dbf1bd2829fb7bf535bbf preference:=1.0

    HTTP/1.1 201 Created
    Content-Length: 149
    Content-Type: application/json; charset=utf-8
    Date: Sun, 14 Dec 2014 16:54:00 GMT
    Location: /ratings/548dc0a8d2829fb7bf535bc2
    Server: Jetty(7.6.8.v20121106)

    {
        "_id": "548dc0a8d2829fb7bf535bc2",
        "created_at": 1418576041,
        "item_id": "548cdd3ed282a2a47b2492a9",
        "preference": 1.0,
        "user_id": "548dbf1bd2829fb7bf535bbf"
    }


Get all ratings

    http GET http://matchbox.nava.de:3000/ratings


Get similar users

    http GET http://matchbox.nava.de:3000/users/548dbf49d2829fb7bf535bc0/similar-users

    HTTP/1.1 200 OK
    Content-Length: 110
    Content-Type: application/json; charset=utf-8
    Date: Sun, 14 Dec 2014 17:01:10 GMT
    Server: Jetty(7.6.8.v20121106)

    {
        "recommended": [
            {
                "_id": "548dbf1bd2829fb7bf535bbf",
                "alias": "heinz",
                "first_name": "heinz",
                "last_name": "Strunk"
            }
        ]
    }


## Sentiments

Analyze a sentence and rate a sentiment.

Current mapping is:

  * strong negative: -5
  * weak negative: -2
  * neutral: 0
  * weak positive: +2
  * strong positive: +5

Example for creating a new sentiment:

    http -v POST localhost:3000/sentiments sentence="I do not like George Clooney and don't like rain." user_id=54cf2fdb0364fac8bb99c08b
    POST /sentiments HTTP/1.1
    Accept: application/json
    Accept-Encoding: gzip, deflate
    Connection: keep-alive
    Content-Length: 104
    Content-Type: application/json; charset=utf-8
    Host: localhost:3000
    User-Agent: HTTPie/0.8.0

    {
        "sentence": "I do not like George Clooney and don't like rain.",
        "user_id": "54cf2fdb0364fac8bb99c08b"
    }

    HTTP/1.1 201 Created
    Access-Control-Allow-Origin: *
    Access-Control-Request-Methods: GET,POST,PUT
    Content-Length: 1022
    Content-Type: application/json; charset=utf-8
    Date: Thu, 05 Feb 2015 11:28:33 GMT
    Location: /sentiments/54d353e10364f68ce067ae77
    Server: Jetty(7.6.13.v20130916)

    {
        "_id": "54d353e10364f68ce067ae77",
        "ratings": [
            {
                "_id": "54d353e10364f68ce067ae78",
                "created_at": 1423135714,
                "item": {
                    "_id": "54d34e1d0364862278b2778b",
                    "name": "rain"
                },
                "item_id": "54d34e1d0364862278b2778b",
                "preference": -5.0,
                "sentiment": "I do not like George Clooney and don't like rain.",
                "user": {
                    "_id": "54cf2fdb0364fac8bb99c08b",
                    "alias": "werner",
                    "first_name": "Werner",
                    "last_name": "Schneeberger"
                },
                "user_id": "54cf2fdb0364fac8bb99c08b"
            },
            {
                "_id": "54d353e10364f68ce067ae79",
                "created_at": 1423135714,
                "item": {
                    "_id": "54d34e1d0364862278b2778d",
                    "name": "George Clooney"
                },
                "item_id": "54d34e1d0364862278b2778d",
                "preference": -5.0,
                "sentiment": "I do not like George Clooney and don't like rain.",
                "user": {
                    "_id": "54cf2fdb0364fac8bb99c08b",
                    "alias": "werner",
                    "first_name": "Werner",
                    "last_name": "Schneeberger"
                },
                "user_id": "54cf2fdb0364fac8bb99c08b"
            }
        ],
        "sentence": "I do not like George Clooney and don't like rain.",
        "user": {
            "_id": "54cf2fdb0364fac8bb99c08b",
            "alias": "werner",
            "first_name": "Werner",
            "last_name": "Schneeberger"
        },
        "user_id": "54cf2fdb0364fac8bb99c08b"
    }



## API Ideas

    POST /ratings -> neue Vorliebe anlegen 
    GET /ratings -> liefert alle vorhandenen Vorlieben zurück

    POST /users   -> neuen User anlegen
    GET /users/<userid> -> liefert User zurück, inkl. seiner Vorlieben
    POST /users/<userid>/ratings  -> hinzufügen einer neuen Vorliebe
    DELETE /users/<userid>/ratings/<rating-id> -> löscht eine Vorliebe

    GET /users/<userid>/similar-users  -> liefert eine Liste mit Usern, die ähnliche Vorlieben haben


JSON für rating:

    {
      "id": "vom-Server-vergeben",
      "name": {
        "en": "Cinema films",
        "de": "Kinofilme",
        "ru": "фильмкинолента"
      }
    }


## Inspirations

Recommender (user/item) system using Mahout and MongoDB, wrapped by a REST service.
https://github.com/nellaivijay/Mahout-MongoDB-Recommender
