# spring-api-challenge

This is an Spring Boot Rest API. It uses PosgreSQL as database and REDIS to cache session.

To run it, download the [docker-compose](https://github.com/virginiafarias/spring-api-challenge/blob/master/challenge/docker-compose.yml) file and execute `docker-compose up`. After that, app will be running on `http://localhost:8080`

You can use the [postman collection](https://github.com/virginiafarias/spring-api-challenge/blob/master/IA-Challenge.postman_collection.json) to call the endpoints.

APP Docker image is available [here](https://hub.docker.com/r/virginiafsousa/spring-ia-challenge).

Users on database are:

| NAME | LOGIN | PASSWORD |
| ------ | ------ | ------ |
| User1 | `user1` | `123` |
| User2 | `user2` | `123` |
| User3 | `user3` | `123` |
