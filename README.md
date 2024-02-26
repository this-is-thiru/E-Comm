## Memory Settings for IntelliJ Idea:
* Menubar -> Help -> Change Memory Settings --- enter 8000
* Menubar -> Help -> Edit Custom Properties: add these
```
idea.max.content.load.filesize=10000
idea.max.intellisense.load.filesize=1500
```

## Plugins to add:
* ###  Eclipse Code Formatter:
  * Install Eclipse Code Formatter plugin.
  * Settings -> Eclipse Code Formatter -> set Eclipse Formatter config to ```Java Conventions```
* ###  Codeium:
    * Install Codeium. - https://codeium.com/
    * Very useful to auto complete when we are working on code.
* ###  POJO to JSON:
    * Install POJO to JSON plugin.

## Build project without Tests:
* #### Note: Demo Unit Tests and Integration Tests has been implemented in Order Service.
* Go to Maven -> Execute maven goal -> execute the command
```bash
mvn clean install -DskipTests=true
```
* By default, it runs all uts as we have configured the tests property as ut. If we want to run ITs we can run the command as below
```
mvn clean install -P it
```


## Test Api Gateway:

1. Run Discovery Server
2. Run Api Gateway
3. Run Seller Service
* Try to make api call with following url:
```angular2html
http://localhost:8080/api/seller/
```
```angular2html
http://localhost:8080/eureka/web
```
Note: For now I have implemented it with Eureka Client only on seller service and api gateway service.

## Run specific module with Maven commandline:
mvn spring-boot:run -pl <module-name>
```bash
mvn spring-boot:run -pl discovery-server
```



\
\
\
Collections for Future Improvements:
1. https://2much2learn.com/mavengradle-based-multi-module-spring-boot-microservices
2. https://medium.com/zero-equals-false/protect-rest-apis-with-spring-security-reactive-and-jwt-7b209a0510f1
