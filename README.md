# Spring-Cloud-Microservices - Currency Conversion Platform

## Introduction

This project demonstrates various features of a Spring Cloud-based microservices architecture for managing and converting currencies. The platform includes several microservices, each responsible for specific functionalities. Below, you will find an overview of each microservice and its role in the project.

## Project Structure

The project consists of the following microservices:

**limits-service:** This microservice handles configuration management using the Spring Cloud Config Server. It fetches configuration values from the local Git repository through the Spring Config Server. It also includes three different profiles for the application properties, each providing different min/max values based on the active profile.

**currency-exchange-service:** The Currency Exchange Service provides currency exchange rates and interacts with the Spring Cloud Config Server for configuration management. It includes an endpoint that allows users to retrieve exchange rates between different currencies. The service is resilient using the Resilience4j library for handling failures.

**currency-conversion-service:** The Currency Conversion Service handles currency conversion using Feign Rest Client to communicate with the Currency Exchange Service. It also implements Resilience4j for fault tolerance. 

**api-gateway:** The API Gateway serves as the entry point to the microservices architecture. It implements intelligent routing and load balancing using Spring Cloud Load Balancer. It communicates with Eureka Naming Server for service discovery and uses Resilience4j for fault tolerance.

**naming-server:** The Eureka Naming Server provides service discovery for microservices in the architecture.

**zipkin-server:** The Zipkin Distributed Tracing Server helps monitor services and servers through distributed tracing.

The API is documented using Swagger and includes HATEOAS links to facilitate navigation.
The microservices are running as Docker containers, and Kubernetes is managing their orchestration. 
