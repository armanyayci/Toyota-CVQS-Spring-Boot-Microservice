

# Toyota CVQS Spring Boot Microservice Project
This repository is a CVQS (Complete Vehicle Quality System) application for Toyota's kickoff recruitment process.

## Table of Content

* [Description](#description)
* [Used Technologies](#used-technologies)
* [Points of Attention](#points-of-attention)
* [Database Design](#database-design)
* [Application Architecture](#application-architecture)
  * [Api](#api)
  * [Service](#service)
  * [Dao](#dao)
  * [Domain](#domain)
  * [DTO](#dto)
* [Security](#security)
  * [Login](#login)
  * [Request to AnyEndpoint](#request-to-anyendpoint)
* [Microservice Architecture](#microservice-architecture)
  * [ApiGateway](#apigateway)
  * [Discovery-Server](#discovery-server)
  * [Token Service](#token-service)
  * [Admin-Terminal Service](#admin-terminal-service)
  * [Operator-Teamleader-Service](#operator-teamleader-service)
  * [Notification-Service](#notification-service)
* [Unit Tests](#unit-tests)
* [Docker](#docker)
  * [Docker Hub](#docker-hub)
  * [Docker Compose](#docker-compose)
* [Contributor](#contributor)


<a name="description"></a>
## Description

The project is developed to enable effective control and management of errors occurring in vehicles upon their departure from the factory. It encompasses various functionalities, including the recording of errors detected in vehicles leaving the factory and the subsequent tracking of these issues. Within this project, which promotes collaborative teamwork, each user assumes a distinct role tailored to their specific characteristics.
These roles encompass a range of responsibilities and expertise. Some users may specialize in error detection and analysis,diligently identifying and documenting any issues found during the final inspection of vehicles. Others may focus on tracking and monitoring these recorded errors, ensuring timely resolution and preventing their recurrence in future production cycles.

In summary, the project aims to optimize error control and management in vehicle manufacturing. By assigning specific roles to each team member, fostering collaboration, and utilizing data-driven insights, the project endeavors to enhance the overall quality of vehicles leaving the factory and ensure customer satisfaction.


<a name="used-technologies"></a>
## Used Technologies

- AmazonCoretto-JDK:17.0.6
- Maven 4.0.0
- Spring Boot 2.7.9
  - Web
  - Data
- Hibernate
- PostgreSQL
- Log4j
- JUnit5
- Lombok
- Docker
- Spring Cloud (Apigateway, Eureka, Kafka, Zipkin)

<a name="points-of-attention"></a>
## Points of Attention

* Using the spring boot framework and using hibernate for object relational mapping.
* N-tier architecture
* Object Oriented Programming
* Accordance with SOLID Principles
* Logging
* Unit test
* Java Doc
* Microservice Architecture and docker environments.

<a name="database-design"></a>
## Database Design
The database design can be examined in 3 different ways. These are users and roles, vehicles, defects and location information, and last terminal and category relationships, which we can divide into 3 main headings.
* User and Role Table:
  * It is a table that shows the users and what roles these users have. A user can have at least one role or more roles. For this reason, the relationship between them is set up as many-to-many. Name, email, username, password and isactive information are kept for User Table. For the role table, there is only name information.
* Vehicle, Defect and Location Table
  * we will examine 3 different tables here. The first of these is the vehicle table. this table holds information such as the name of the vehicles, model year, vehiclebody, etc. Establishes a one-to-many relationship with the VehicleDefect table.
  * The VehicleDefect table, which is our second table, keeps the information of the vehicle defect in it. These are the description and the picture of the error. This table establishes a one-to-many relationship with the DefectLocation table. It should be noted here that there may be more than one error location on an image.
  * Our last table is the DefectLocation table. This is where the locations on the defect image saved to the vehicle are stored in this table. In this table, 3 different (x,y) coordinates are kept.
* Terminal and TerminalCategory Table
  * Terminal table keeps the names of the active terminals on system, while the TerminalCategory specifies which category these terminals belong to, there is a one-to-many relationship between them.
    <br>
    The database diagram is given below.
    <br><br>
    <img src="https://i.hizliresim.com/tti6qtj.jpg" width="900" height="780">

<a name="application-architecture"></a>
## Application Architecture

The project architecture consists of 5 layer.

  <img src="https://i.hizliresim.com/tl0iinc.png" width="800" height="400">

<a name="api"></a>
### Api

The API layer is responsible for defining the endpoints and handling HTTP requests and responses. It acts as a bridge between the client and the underlying layers of the application. The API layer is implemented using controllers, which handle incoming requests, invoke the appropriate service methods, and return the response.

<a name="service"></a>
### Service

The service layer contains the business logic of the application. It encapsulates the complex operations and coordinates the interaction between different components. Services are responsible for implementing use cases, applying business rules, and orchestrating data access. They are typically used by the API layer to perform specific operations requested by the client.

<a name="dao"></a>
### DAO

The DAO layer is responsible for data access and persistence. It interacts directly with the database. DAOs provide methods for querying, creating, updating, and deleting data. They abstract the underlying data storage and provide a simplified interface for the service layer to interact with the database.

<a name="domain"></a>
### Domain

The domain layer represents the core business entities and concepts of the application. It contains the domain models and entities that reflect the real-world objects the application deals with. These models encapsulate the business rules and behaviors associated with the entities.


<a name="dto"></a>
### DTO

The DTO layer provides a structured way to transfer data between different layers of the application. DTOs are simple objects that hold data and have no behavior. They act as data containers used to transport data between the API layer and service layer. DTOs help in decoupling the layers and allow for better control over the data being transferred.
<br>

<a name="security"></a>
## Security
A role-based authentication and authorization structure has been created using the jwt token to provide access to the relevant endpoints. This structure is provided using the <a href="https://github.com/jwtk/jjwt">JJWT library.</a> There are 3 different roles in accessing endpoints:
* Admin
* Operator
* Teamleader

<a name="login"></a>
### Login

When user wants to log in user sends a request to the corresponding endpoint to log in, according to the user credentials accuracy and if the activeness of user is true, a unique token is generated for that user, and he can access the endpoints according to the permission of the endpoint roles related to this token.

Login Diagram:

<img src="https://i.hizliresim.com/5ftbb9j.jpg" >

<a name="request-to-anyendpoint"></a>
### Request to AnyEndpoint

When the user logs in and the token is generated, the user's credentials and roles are given to the token as a claim.
Then, when he sends a request to the endpoint with this token, the claims in this token are parsed and redirected to the endpoint according to the accuracy of the user's credentials and the adequacy of his role.
While checking the accuracy of the user's token,a request is sent to the <strong>/auth/check/{token}</strong> endpoint of the Token Service and a boolean value is returned. If this value is true,
the user accesses the corresponding endpoint. Otherwise ,the user will encounter <strong>401 Unauthorized.</strong>

Request to AnyEndpoint Diagram:

<img src="https://i.hizliresim.com/3c6jue8.jpg" >

<a name="microservices-architecture"></a>
## Microservice Architecture

<img src="https://i.hizliresim.com/gqxoxpu.png" width="800" height="600">

<a name="apigateway"></a>
### ApiGateway

API Gateway acts as an entry point in microservices architecture, receiving incoming requests and routing them to the appropriate microservices running in the background. It serves as a bridge between clients and microservices. Clients interact with the services through the API Gateway, which handles tasks like request authentication, rate limiting, request/response transformation, and caching. By consolidating multiple microservices behind a single API Gateway, it simplifies the client-side communication and provides a centralized control point for managing various aspects of the API.

<a name="discovery-server"></a>
### Discovery Server

In microservices architecture, where services are distributed and dynamically scaled, a Discovery Server plays a crucial role in service registration and discovery. The Discovery Server acts as a centralized registry or directory where microservices can register themselves and advertise their availability. It allows services to discover and communicate with each other without prior knowledge of their network locations. The Discovery Server maintains an up-to-date registry of all available services and provides a mechanism for service instances to dynamically register, deregister, and update their information. This enables dynamic scaling, load balancing,and fault tolerance within the microservice's ecosystem.

<a name="token-service"></a>
### Token Service

Token service is the place where security transactions are performed in it, checking whether the credentials of this user are correct when the user logs in. The token based authentication and authorization structure was created using the JWT library. When the user logs in successfully, a unique token is generated for the user. There is a validity period for this token, and if this period does not expire, requests can be made to the endpoints related to the token. At the same time, it is the place where roles are claimed into the token while providing this control in the role control section in Apigateway.

<a name="admin-terminal-service"></a>
### Admin-Terminal Service

* Admin Service

  The Admin service is the place where user management operations are performed. Here; <strong>adding users, updating, deleting (soft), activating the deleted user, adding roles, deleting roles, listing users, and fetching users with an id</strong> are included. The admin role is required for these operations. It is made with dto objects.

* Terminal Service

  It is the service that lists the active terminals in the system. There is no need for authorization for this service. Anyone who has logged in can access here.


<a name="operator-teamleader-service"></a>
### Operator Teamleader Service

* Operator


The operator service allows upload defects to vehicles. It is the place where the coordinates and explanation of the vehicle defect on the picture are given in json to provide tracking of the faulty vehicles leaving the factory by giving the vehicle's image as a MultipartFile. Three (x,y) coordinates are saved to the system. It also allows you to save more than one error to the image on a vehicle. Only the users who have operator role can access this service.

An example uploading process would be like this through postman.

<img src="https://i.hizliresim.com/21qh896.jpg" width="800" height="330">

* Teamleader


The Teamleader service performs a drawing operation based on the locations saved on the image saved by the operator to the database and displays it on the postman. In addition, it provides the <strong>the error list of vehicles by filtering, paging and sorting and the drawn image of defect</strong>. Only users with the teamleader role can access this service.

An example draw response according to the operator's upload would be like this.

<img src="https://i.hizliresim.com/aafm063.jpg" width="800" height="480">

<a name="notification-service"></a>
### Notification Service

When the user is registered by the admin, the admin service is listened to by the notification-service asynchronously with the kafka and Event Driven Architecture. The user information registered with this communication is sent as a message by kafka and an e-mail is sent to the registered user using this information by the notification service.

<img src="https://i.hizliresim.com/8wbcced.jpg" width="656" height="317">

<a name="unit-tests"></a>
## Unit Tests

The testing aspect of this project plays a crucial role in ensuring the reliability and quality of the codebase. Spring Boot provides excellent support for writing and executing tests, making it easier to maintain the codebase and catch bugs early in the development cycle.

The project utilizes popular test frameworks such as <strong>JUnit5 and Mockito</strong> to create comprehensive and reliable tests. These frameworks offer a wide range of annotations, assertions, and utilities to facilitate test writing and verification.

The project contains a dedicated package for test classes, which follows the same package structure as the main source code. This organization helps in maintaining a clear separation between production and test code. The test classes are suffixed with Test to distinguish them from regular classes.

* Coverage

  The project aims for comprehensive test coverage to minimize the risk of undiscovered bugs. Critical sections such as business logic, and error handling are prioritized for thorough testing.

<img src="https://i.hizliresim.com/mbim3ia.jpg">

<a name="docker"></a>
## Docker

[Docker](https://www.docker.com) is an open platform for building, shipping and running distributed applications. Follow steps on the site to install docker based on your operating system.

<a name="docker-hub"></a>
### Docker Hub

[Click Here](https://hub.docker.com/u/zormanfayci) if you want to check my Dockerhub repository.

<a name="docker-compose"></a>
### Docker Compose

Docker Compose is a tool that allows you to define and manage multi-container Docker applications. It simplifies the process of running and building multiple containers together as a single application. In the context of a Spring Boot cloud application, Docker Compose can be used to define and manage the deployment of various services and components that make up the application's architecture.


* Run the application using docker compose. <strong>(ensure that you arrange database connection as your local)</strong>


    ```bash 
      docker compose up
    ``` 
* Stop the running application.


    ```bash 
      docker compose down
    ```

<a name="contributor"></a>
## Contributor

This project is made by Arman Yaycı. For questions and error notification contact with me. armanyayci@icloud.com




