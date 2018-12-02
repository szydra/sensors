FROM openjdk:8-jdk-alpine
LABEL maintainer="szymon.draga@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/sensors-0.1.jar
COPY ${JAR_FILE} sensors.jar
COPY application.properties application.properties
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/sensors.jar"]