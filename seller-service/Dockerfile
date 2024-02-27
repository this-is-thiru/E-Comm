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
COPY --from=build /target/seller-service-1.0-SNAPSHOT.jar seller-service.jar
# ENV PORT=8081
EXPOSE 8081
ENTRYPOINT ["java","-jar","seller-service.jar"]