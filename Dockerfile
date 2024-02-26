FROM openjdk:21-jdk
EXPOSE 8080
VOLUME /tmp
ARG JAR_FILE=target/*.jar
ADD ./discovery-server/target/discovery-server.jar discovery-server.jar
ENTRYPOINT ["java","-jar","/spring-security.jar"]