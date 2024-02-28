
#
# Build stage
#
FROM maven:3.9.5-eclipse-temurin-21 AS build
COPY .. .
RUN mvn clean install -DskipTests

#
# Package stage
#
FROM openjdk:21-jdk
COPY --from=build /target/discovery-server.jar discovery-server.jar
# ENV PORT=8761
EXPOSE 8761
ENTRYPOINT ["java","-jar","discovery-server.jar"]