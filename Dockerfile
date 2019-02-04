FROM openjdk:8-jdk-alpine
LABEL maintainer="szymon.draga@gmail.com"
VOLUME /tmp
EXPOSE 8080
COPY target/sensors*.jar sensors.jar
COPY application.properties .
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/sensors.jar"]