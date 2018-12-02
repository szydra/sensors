# sensors
## Overview
The repository contains a Spring Boot application designed for sensor management. It provides the functionality of downloading
a sensor list from the network on server startup as well as a REST API for updating sensor values.

In-memory database H2 is used, however there is no problem with connecting the application to a real database.

## How to run
The application may be run in different ways.
1. Run with Spring Boot Maven Plugin
    
    by executing the following Maven command:
    ```
    mvn spring-boot:run -Dspring-boot.run.arguments=--sensors.yaml.url=https://github.com/relayr/pdm-test/blob/master/sensors.yml
    ```
2. Run as a jar file

    by compiling and packaging the project via `mvn package` and then by running the built jar file:
    ```
    java -jar target/sensors-0.1.jar --sensors.yaml.url=https://github.com/relayr/pdm-test/blob/master/sensors.yml
    ```
Obviously, in the above examples the value of `sensors.yaml.url` can be replaced with any valid URL to a YAML file with sensors data.
In case of an invalid URL the application fails to run, however, one can omit this parameter to disable loading data on startup.

## JUnit tests
Unit and integration tests may be run by usual Maven command `mvn test`. For more fluent and readable assumptions AssertJ library is used.

According to IntelliJ IDEA the current line coverage equals 89%.

## Docker
The application may be run as a Docker container as well. Its image can be built using the `Dockerfile`
or can be downloaded from Docker Hub via the following command:
```
docker pull szydra/sensors
```
Then one can run a container and pass a URL to a YAML file with sensors data as an environment variable `SENSORS_YAML_URL`, e.g.:
```
docker run -e "SENSORS_YAML_URL=http://github.com/relayr/pdm-test/blob/master/sensors.yml" -p 8080:8080 szydra/sensors
```
