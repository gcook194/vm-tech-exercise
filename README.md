# Virgin Money Tech Test

## General Design
The application is a Spring Boot 3 REST API written using java 17. There is quite a lot of boilerplate as is always the way with Java projects but this ReadMe should contain all the information you will need to build, run and test the code. 

The entry point for the application is the web layer, below this sits the service layer and below this sits the repository layer.

The main Transaction data controller class lives at `src/main/java/com/virginmoney/transactionlog/web/TransactionController.java`

For the most part, the web layer really only deals with handling requests and responses and very little else. The service layer hits the repository layer to retrieve data from the database then converts responses into DTOs using MapStruct mapper classes, and the repository layer is where all of the interactions with the database happens.

## Starting the app

The application is a maven project so to run it you will either need to be using an IDE such as Intellij which comes pre-packaged with Maven, or you will need to have Maven installed globally on your local machine. In either case, once Maven is installed, you can run the following command to start the application. 

```
mvn spring-boot:run
```

## Postman
There is a Postman collection in the `resources/postman` directory. It contains one example of every request asked for as part of the brief (as well as a few additional ones added to aid in testing).

If you choose to use the collection to test the code you'll need to retrieve a bearer token using the "Get Bearer Token" request. This token can then be added into the Collection Variables and the other requests should work fine.

If you do not use Postman then you'll have to follow the instructions in the Login section below. 

## Login

The project currently uses an in memory `UserDetailsManager` with a single hard coded user. This choice was made to save on time while still being roughly representative of how a production application would handle user management. To take the application into a production ready state, this bean would need to be replaced with one that either connects to the database or a third party service such as Google or Okta in order to fetch user credentials.

Endpoints in the project are secured using self-signed JWT tokens. The tokens last one hour and can only be issued to users who have authenticated with the system. In the case of this project there is only one user who can authenticate. Credentials can be found below. 

```
username: gcook
password: password
```

More details about the Spring Security flow for the project can be found in [this wonderful tutorial by Dan Vega](https://www.danvega.dev/blog/spring-security-jwt). 

In order to log into the application you need to retrieve a JWT token by sending a HTTP POST request to the following endpoint. You will need to use basic auth to provide the username and password listed above when sending the request. If you have something like Postman installed then this is very simple and can be done through the **Authorization** tab inside the request builder.

```
http://localhost:8081/api/token
```
If you are using Curl then the following command should get you a token 

```
curl http://gcook:password@localhost:8081/api/token -X POST
```
From here, the token retrieved should be passed as a bearer token in every request. In Postman this is again very simple and can be configured through the UI. If using Curl then [this tutorial](https://reqbin.com/req/c-hlt4gkzd/curl-bearer-token-authorization-header-example) should help. 


## Testing the app

The application comes packaged with a set of unit tests as well as a small set of Cucumber tests. To run all tests use the command below.

**Run all tests:**

```
mvn clean verify
```

## Database schema & migrations

The project uses a H2 in memory database with the schema being created and synchronised using Flyway.
Spring will automatically run new migrations on start up, but this can also be done manually through maven.
Credentials needed by Flyway are stored in `pom.xml`, within Flyway's plugin definition.
SQL scripts are stored in `src/main/resource/db.migration` and to run the migration setup
using Maven run the following command:

```
mvn flyway:migrate
```

This will run all scripts in the directory listed above. Migration history is stored in a table called
`flyway_schema_history`. This means that whenever a new script is added to the directory, and you run the Maven migration
command again, it will only run the scripts that have been added since the last time Flyway updated this table.

Each time the application starts up, the in-memory database is seeded with test data found in the migration file named `V2__insert_sample_user_transaction_data.sql`

## API Docs
Auto generated API documentation is available to view at the following URL 
```
http://localhost:8081/api/swagger-ui/index.html
```

This documentation is also available in JSON format at the following location 
```
http://localhost:8081/api/v3/api-docs
```

## CI Pipeline
There is a very basic Github Actions workflow file located at `.github/workflows/pipeline.yml`. Github automatically looks for these files and runs them based on the conditions specified in the file. In our case the pipeline runs on any push to main. 

The pipeline doesn't put the code anywhere but it does install and cache dependencies then runs unit and integration tests. In a production solution, the next steps would obviously be things like security scans, Linting and pushing artefacts to cloud providers using things like Docker. 

The output of the Github Actions runs can be viewed using the following URL: 

https://github.com/gcook194/vm-tech-exercise/actions

## Ramblings

### Unit Tests
I have chosen to use `AssertJ` assertions instead of the basic jUnit ones as I think these are more readable and more explicitly demonstrate 
what is being tested. 

I have chosen to use `jUnit` and `AssertJ` assertions to test the web layer. I often see people using `MockMVC` but in my mind these are more like integration tests as they start up a partial Spring Context and offer a lot of Spring behaviours. I prefer keeping my unit tests as simple as possible and integration tests elsewhere. 

### Integration Tests
Not all functionality is fully integration tested, however a number of 
the endpoints in the application are covered to demonstrate competency. 

I haven't actually used Cucumber in a production codebase before. All the teams I have worked on have had standalone Cypress based integration and e2e suites but for the sake of keeping everything inside the project and keeping it in Java I chose to add Cucumber.

### DTOs & Object Mapping
I have chosen to use MapStruct to map between JPA entities and DTOs to save time but also because I feel like this 
is a solved problem. MapStruct is the best library I have used to map 
between objects and DTOs, and it offers the most flexibility in terms of customisation and mixing automatic conversion 
with manual conversion. 

DTOs are all written using Java 17 Records as these are immutable so are ideal for passing around in larger projects..

### JPA
Although the project uses JPA repositories and mappings, the majority of queries are actually written using raw SQL. This was simply due to a number of the requirements asking for aggregations to be performed on the data and I find it easier doing this in SQL. 

This also gave me the opportunity to use JPA projection interfaces for the first time.

### Application properties 
I have chosen to use Yaml as in my experience `application.properties` files can become messy and difficult to maintain
in a way that YAML files just don't ever seem to.
