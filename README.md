## Setting up the project:
clone the repository with the following command
```bash
git clone --recurse-submodules https://github.com/this-is-thiru/E-Comm.git
```
If we want to clone a specific branch we can use the following
```bash
git clone --recurse-submodules https://github.com/this-is-thiru/E-Comm.git --branch git-submodule
```
### Note:
For better understanding of the setup of this project go through this [doc](https://onedrive.live.com/personal/4fb5a8d7e4d1ea59/_layouts/15/doc2.aspx?resid=64f229a5-6716-4438-b374-827559f3563c&cid=4fb5a8d7e4d1ea59&ct=1713549290218&wdOrigin=OFFICECOM-WEB.START.EDGEWORTH&wdPreviousSessionSrc=HarmonyWeb&wdPreviousSession=110d2918-5730-4577-bb27-047664beb6bf)

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
* Go to Maven -> select desired profiles to get activated
* Execute maven goal -> execute the command
```bash
mvn clean -s settings.xml install -DskipTests=true
```
* By default, it runs all uts as we have configured the tests property as ut. If we want to run ITs we can run the command as below
```
mvn clean -s settings.xml install -P it
```


## Test Api Gateway:

1. Run Discovery Server (running online on render for now)
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



\
\
\
Collections for Future Improvements:
1. https://2much2learn.com/mavengradle-based-multi-module-spring-boot-microservices
2. https://medium.com/zero-equals-false/protect-rest-apis-with-spring-security-reactive-and-jwt-7b209a0510f1
3. https://github.com/2much2learn/article_dec_28_mavengradle-based-multi-module-spring-boot-microservices



Submodules Sync with master branch changes (We can run this command every week to get updated changes):
```bash
git submodule foreach "(git pull origin master; cd ..;)"; git add .; git commit -m "Submodule sync with master branch"; git push
```

This script will raise the empty commit for each submodule. This will help the re trigger the build in production.
```bash
git submodule foreach "(git checkout -b temp; git pull origin master; git commit -m 'Empty commit for test'; git push; cd ..;)";
```

Commit changes to all submodules at the same time
```bash
git submodule foreach "(git switch master; git add '.'; git commit -m '<message>'; git push origin master; cd ..)"
```
https://stackoverflow.com/questions/1777854/how-can-i-specify-a-branch-tag-when-adding-a-git-submodule/18799234#18799234

