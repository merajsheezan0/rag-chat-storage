# RAG Chat Storage Microservice

This microservice stores chat sessions and messages for a **RAG-based chatbot** system.

## Features

- Create, rename, delete chat sessions
- Add and retrieve chat messages (paginated)
- Mark/unmark sessions as favorite
- API key authentication
- Rate limiting
- Swagger/OpenAPI documentation
- Dockerized with MySQL and Adminer
- Centralized logging** and global error handling
- Health check endpoints using Spring Boot Actuator

## Prerequisites

- Docker & Docker Compose
- Java 21
- Maven (to build the jar)

## Setup

1. Clone the Repository

bash
git clone https://github.com/merajsheezan0/rag-chat-storage.git
cd rag-chat-storage

2. Copy `.env.example` to `.env` and edit if needed:

bash
cp .env.example .env

| Method | Endpoint                  | Description                     |
| ------ | ------------------------- | ------------------------------- |
| POST   | `/sessions`               | Create a new chat session       |
| GET    | `/sessions`               | List all sessions               |
| PUT    | `/sessions/{id}/rename`   | Rename a session                |
| PUT    | `/sessions/{id}/favorite` | Mark/unmark session as favorite |
| DELETE | `/sessions/{id}`          | Delete a session                |

| Method | Endpoint    | Description                               |
| ------ | ----------- | ----------------------------------------- |
| POST   | `/messages` | Add a message to a session                |
| GET    | `/messages` | Get messages for a session                |
|        |             | Supports `page` and `size` for pagination |


# Health check
GET /actuator/health

# Swagger UI
http://localhost:8080/swagger-ui.html

# Adminer
http://localhost:8081