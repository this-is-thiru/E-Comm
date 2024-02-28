#
# Build stage
#
FROM maven:3.9.5-eclipse-temurin-21 AS build
#COPY .. .
COPY ../pom.xml /tmp/
COPY ../discovery-server/ /tmp/discovery-server/
WORKDIR /tmp/
RUN mvn clean install -Pdocker

#
# Package stage
#
FROM openjdk:21-jdk
COPY --from=build /tmp/discovery-server/target/discovery-server.jar discovery-server.jar
# ENV PORT=8761
EXPOSE 8761
ENTRYPOINT ["java","-jar","discovery-server.jar"]