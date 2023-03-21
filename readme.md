### How to use this spring-boot project

-   Install packages with `mvn package`
-   Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

-   Swagger UI : http://localhost:8080/swagger-ui.html
-   H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.

### Instructions

-   download the zip file of this project
-   create a repository in your own github named 'java-challenge'
-   clone your repository in a folder on your machine
-   extract the zip file in this folder
-   commit and push

-   Enhance the code in any ways you can see, you are free! Some possibilities:
    -   Add tests
    -   Change syntax
    -   Protect controller end points
    -   Add caching logic for database calls
    -   Improve doc and comments
    -   Fix any bug you might find
-   Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
-   Send us the link of your repository.

#### Restrictions

-   use java 8

#### What we will look for

-   Readability of your code
-   Documentation
-   Comments in your code
-   Appropriate usage of spring boot
-   Appropriate usage of packages
-   Is the application running as expected
-   No performance issues

#### Your experience in Java

-   I have about 4 years of experience developing Java web applications using Spring and 2 years of hands-on experience with Spring Boot.

#### Features added to this project

-   Add Unit tests
-   Secure endpoints with Spring Security and JPA using http basic
-   In memory cache for retrieving employees
-   Swagger documentation
-   JavaDoc and remote site
-   Split Entity / DTO and handle mapping
-   Pagination for retrieving list of employees
-   Custom response model
-   Custom error handling with proper status code
-   Request body validation with Spring
-   Logging instead of System.out

#### What would I have added if I had more time

-   Implement JWT authentication
-   Make employee.department another table to showcase entity relational mapping
-   Add end to end Integration tests with @SpringBootTest
-   Containerize with Docker
-   Connect to an external DB like Postgres running in a docker container

#### Problems remaining

-   Documenting generic Response type with Swagger
-   Swagger UI displaying but generating NoHandlerFound error resulting in error logs in console
-   Unable to publish site to github with maven-scm-publish-plugin (pushed gh-pages branch manually)

### About this application

#### Running the app

-   Run with `mvn spring-boot:run` or with IDE
-   To run with web security disabled use `mvn spring-boot:run -Dspring-boot.run.arguments=--app.security.enabled=false`. You can also edit the property directly in application.properties file and run the app normally.

#### Notes

-   Two fake users are being created on startup. You can use their credentials to access endpoints.
    -   role ADMIN{username=james,password=admin} : Can perform every request
    -   role USER{username=bob,password=user} : Can only consult data (GET operations)
-   Project documentation site is accessible here : https://arthurcc.github.io/java-challenge/
-   Postman collection used for testing the app is accessible in project root directory : Coding-test-AXA.postman_collection.json
