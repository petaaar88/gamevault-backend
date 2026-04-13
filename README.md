# GameValut Backend

GameVault is a platform that simulates a real game distribution service like [Steam](https://store.steampowered.com/).
Registered users can browse available games, view details, and download them directly from the app.
Users can send friend requests and leaving comments on profile. 

This system consists of three parts: Backend (this repository), ["Desktop Client"](https://github.com/petaaar88/gamevault-desktop-client) and ["Admin Client"](https://github.com/petaaar88/gamevault-admin-client)

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Setup Instructions](#setup-instructions)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
- [Run (Development)](#run-development)
- [Containerization (Docker)](#containerization-docker)
- [RESTful API Overview](#restful-api-overview)
  - [Auth](#auth)
  - [Users](#users)
  - [Profile](#profile)
  - [Comments](#comments)
  - [Recent played games](#recent-played-games)
  - [Games](#games)
  - [Collections](#collections)
  - [Checks](#checks)
  - [Admin](#admin)
  - [Launcher](#launcher)
- [WebSockets](#websockets)
- [CORS](#cors)
- [Documentation](#documentation)

## Features
- **User profiles, friends, and comments** — interaction between users.
- **Game catalog** with genres, images, descriptions, reviews, and ratings.
- **Personal game collections** with playtime tracking and activity history.
- **Real-time notifications** for friend requests, online status, and in-game events.
- **Game reviews** with text feedback and numeric ratings.

## Tech Stack
- <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" height="17" title="Java"/> Java 23
- <img src="https://cdn.simpleicons.org/springboot/6DB33F" height="17" title="Spring Boot"/> Spring Boot 3
- <img src="https://cdn.simpleicons.org/mysql/4479A1" height="17" title="MySQL"/> MySQL 8.0 (primary datastore)
- <img src="https://cdn.simpleicons.org/redis/DC382D" height="17" title="Redis"/> Redis 5 (active users)
- <img src="https://raw.githubusercontent.com/simple-icons/simple-icons/develop/icons/socketdotio.svg" height="17" title="STOMP/WebSocket"/> STOMP over SockJS (notifications)
- <img src="https://upload.wikimedia.org/wikipedia/commons/9/93/Amazon_Web_Services_Logo.svg" height="17" title="AWS S3"/> AWS S3 (game files and images)

## Setup Instructions

### Prerequisites
- JDK: Oracle OpenJDK 23.0.1 (recommended)
- MySQL 8.0.40 running and reachable
- Redis 5.0.14.1 running and reachable
- AWS account
- Maven 3.9

### Configuration
This project reads configuration from environment variables.

1. Create `.env` files and copy variables from `.env.example` 
```
AWS_GAME_FILES_BUCKET_NAME=<name_of_bucket>
AWS_GAME_IMAGES_BUCKET_NAME=<name_of_image_bucket>
AWS_USER_PROFILE_IMAGE_BUCKET_NAME=<name_of_user_profile_images_bucket>
MY_AWS_ACCESS_KEY=<aws_access_key>
MY_AWS_SECRET_KEY=<aws_secret_key>

MYSQL_DATABASE_DB_NAME=<connection_url+database_name>
MYSQL_DATABASE_PASSWORD=<password>
MYSQL_DATABASE_USERNAME=<user>

REDIS_HOST_URL=<url>
REDIS_PORT=<port>
```

2. Create Database from schema provided at [`db-dump`](/db-dump) folder. Also you can see ERD model of database in [docs.md](/docs/docs.md) file.

3. Install dependencies with Maven
```
mvn clean install
```

## Run (Development)
```
cd gamevalut-server
```
```
mvn spring-boot:run
```
Ensure MySQL and Redis are running.


## Containerization (Docker)

The entire stack (MySQL, Redis, Backend, Admin Client) can be run with Docker Compose.

### Prerequisites
- Docker and Docker Compose installed

### Configuration
1. Copy `.env.example.docker` to `.env.docker` and fill in the values:
```
AWS_GAME_FILES_BUCKET_NAME=<name_of_bucket>
AWS_GAME_IMAGES_BUCKET_NAME=<name_of_image_bucket>
AWS_USER_PROFILE_IMAGE_BUCKET_NAME=<name_of_user_profile_images_bucket>
MY_AWS_ACCESS_KEY=<aws_access_key>
MY_AWS_SECRET_KEY=<aws_secret_key>

MYSQL_DB_NAME=<database_name>
MYSQL_DATABASE_USERNAME=<user>
MYSQL_DATABASE_PASSWORD=<password>
MYSQL_ROOT_PASSWORD=<root_password>

BACKEND_HOST_PORT=8080
MYSQL_HOST_PORT=3306
REDIS_HOST_PORT=6379

ADMIN_CLIENT_HOST_PORT=80
VITE_API_URL=<backend_url_accessible_from_browser>
```

2. Choose a DB dump file from the [`db-dump`](/db-dump) folder and set `DB_DUMP_FILE` to its filename (e.g. `structure_only_gamevault_db.sql`).

3. Set `COMPOSE_PROJECT_NAME` to any name you like (e.g. `gamevault`).

### Services

| Service      | Image                                       | Default Port |
|--------------|---------------------------------------------|--------------|
| MySQL        | `mysql:8.0`                                 | 3306         |
| Redis        | `redis:7-alpine`                            | 6379         |
| Backend      | Built from `./gamevalut-server/Dockerfile`  | 8080         |
| Admin Client | `petaaar/gamevault-admin-client:latest`     | 80           |

### Run
```bash
docker compose up -d
```

The backend will wait for MySQL and Redis to be healthy before starting. Logs can be followed with:
```bash
docker compose logs -f
```

To stop and remove containers:
```bash
docker compose down
```

---

## RESTful API Overview
### Auth

- POST /login
- POST /register

### Users

- GET /friends/{userId}
- GET /relationship/{userId}/{friendId}
- GET /search/{userId}?username=&page=&limit=

### Profile

- GET /profile/{userId}
- PATCH /profile/{userId}

### Comments

- POST /profile/comments/{userId}/{friendId}
- GET /profile/comments/{userId}

### Recent played games

- GET /profile/games/{userId}

### Games

- GET /games?page=&limit=&title=
- GET /games/genres
- GET /games/{id}/images
- GET /games/{id}/reviews
- GET /games/{id}/pp-images
- GET /games/{id}/description
- GET /games/{id}/download

### Collections

- GET /games/collection/{userId}
- GET /games/collection/{userId}/{gameId}
- POST /games/{gameId}/{userId}
- PATCH /games/collection/{userId}/{gameId}

### Checks

- GET /games/{userId}/{gameId}/has-game
- GET /games/{gameId}/{userId}/has-review
- GET /games/{userId}/{gameId}/has-friends-that-own-game
- GET /games/{id}/has-reviews

### Admin

- POST /games
- POST /games/genres
- POST /games/{gameId}/system-requirements
- POST /games/{gameId}/image
- GET /games/unpublished
- GET /games/unpublished/{id}
- DELETE /games/unpublished/{id}/delete

### Launcher

- POST /play/{userId}/{gameId}
- DELETE /play/{userId}

## WebSockets
- STOMP endpoint: `/ws` (SockJS enabled)
- Subscribe to topics:
  - `/user-online-notification/{userId}`
  - `/friend-request-notification/{userId}`
  - `/friend-entered-game-notification/{userId}`
- Typical client flow: connect via SockJS → STOMP CONNECT → SUBSCRIBE to topics → receive JSON payloads with friend/user info


## CORS
Allowed origins (dev): `http://localhost:3000`, `4200`, `8080`, `5173`

### Documentation

You can see detailed documentation in [`docs.md`](/docs/docs.md)
