# Spring Boot Microservices Project

## Overview

This project implements a microservice architecture using Spring Boot applications. It includes a gateway, discovery and config servers, along with inter-service communication and a RabbitMQ message broker.

## Features

- **Microservice Architecture**: Spring Boot applications following microservice patterns.
- **Gateway**: A gateway server to route requests and implement a load balancer using Spring Cloud Gateway.
- **Discovery & Config Servers**: Service discovery and centralized configuration management using Eureka and Spring Cloud Config.
- **Inter-Service Communication**: REST API calls between microservices using the OpenFeign client.
- **RabbitMQ Integration**: Message producer and consumer setup using RabbitMQ.
- **Spring Security**: Basic authentication with user details stored in a database using Spring Security.
- **Zipkin**: Distributed tracing with Zipkin.

## Setup

1. Clone the repository.
2. Run the `docker-compose up` command in the root directory for the required dependencies.
3. Run the microservices.
4. Access the services at the following URLs:
   - Gateway: `http://localhost:8084`
   - Discovery/Eureka Server: `http://localhost:8761`
   - Config Server: `http://localhost:8888`
   - Zipkin Server: `http://localhost:9411`
   - RabbitMQ: `http://localhost:15672` (username: `guest`, password: `guest`)

*All the apis are to be consumed from the gateway server.*

## API Documentation

The API documentation can be found oneline at <https://documenter.getpostman.com/view/20437995/2sA3QpDZa7> or also the [postman collection](Spring%20microservice%20APIs.postman_collection.json) is provided in the root directory. Import the collection in Postman to test the APIs.
