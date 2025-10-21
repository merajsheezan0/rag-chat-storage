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

## Prerequisites

- Docker & Docker Compose
- Java 21
- Maven (to build the jar)

## Setup

1. Copy `.env.example` to `.env` and edit if needed:

```bash
cp .env.example .env
