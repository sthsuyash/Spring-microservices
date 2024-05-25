# Spring Boot Microservices Project

## Overview

This project implements a microservice architecture using Spring Boot applications. It includes a gateway, discovery and config servers, along with inter-service communication and a RabbitMQ message broker.

## Features

- **Microservice Architecture**: Spring Boot applications following microservice patterns.
- **Gateway**: A gateway server to route requests and implement a load balancer.
- **Discovery & Config Servers**: Service discovery and centralized configuration management.
- **Inter-Service Communication**: REST API calls between microservices.
- **RabbitMQ Integration**: Message producer and consumer setup using RabbitMQ.
- **Spring Security**: Basic authentication with user details stored in a database.
- **Zipkin**: Distributed tracing with Zipkin.
- **Docker & Docker Compose**: Containerized applications with Docker and Docker Compose.

## API Documentation

The API documentation can be found at `https://documenter.getpostman.com/view/20437995/2sA3QpDZa7` or also the postman collection is provided in the root directory.

## Setup

1. Clone the repository.
2. Run the `docker-compose up` command in the root directory.
3. Access the services at the following URLs:
   - Gateway: `http://localhost:8080`
   - Discovery Server: `http://localhost:8761`
   - Config Server: `http://localhost:8888`
   - Zipkin Server: `http://localhost:9411`
   - RabbitMQ: `http://localhost:15672` (username: `guest`, password: `guest`)
   - Eureka Client: `http://localhost:8081`
   - Config Client: `http://localhost:8082`
