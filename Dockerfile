#
# Build stage
#
FROM maven:3.9.5-jdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:21-jdk
COPY --from=build /discovery-server/target/discovery-server.jar app.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]