#
# Build stage
#
FROM maven:3.9.5-eclipse-temurin-21 AS build
COPY .. .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:21-jdk
COPY --from=build /target/api-gateway-service.jar gateway-service.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","gateway-service.jar"]