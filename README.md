# flights-api

## Local setup

### Required dependencies

1. [JDK17](https://www.oracle.com/java/technologies/downloads/#java17)
2. [Maven-3.9.6](https://maven.apache.org/download.cgi)
3. [Docker-4.25.2](https://docs.docker.com/desktop/install/windows-install/)
4. [Postgres-16.1](./docker-compose.yaml)
5. [MongoDB-5.0.23](./docker-compose.yaml)
6. [RedisDB-7.2.3](./docker-compose.yaml)
7. [Intellij IDE-2023.2](https://www.jetbrains.com/idea/download/other.html)

### IDE Setup

1.Download [code style xml](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)
2. click on ```ctrl+alt+s```, Select ```Editor``` --> ```Code Style``` --> ```Java```
3. Click on Settings Icon, beside the
   Scheme --> ```Import Scheme``` --> ```Intellij IDEA Code Style XML```
4. Select above downloaded file path

### Running Application in Local

1. In Terminal run ```docker-compose up```
2. Run the command for DB details encryption ```mvn jasypt:encrypt "-Djasypt.encryptor.password=ardorpass" "-Djasypt.plugin.path=file:src\main\resources\application-dev.yaml"```
3. Open Terminal and run ```mvn clean install "-Djasypt.encryptor.password=ardorpass"``` to install all the required dependencies.
4. Set up the mongo user in local by running the below command
    1. ```docker exec -it mongodb bash```
    2. ```mongo -u mongoroot -p M0ng0@dm1n``` [Note:- Take user name and password from docker-compose.yaml]
    3. ```use ardordb```
    4. ```db.createUser({user: "appUser", pwd: "ard0r@pp", roles : [{role: "readWrite", db: "ardordb"}]});```
5. Run the Application main class with vm arguments as ```-Djasypt.encryptor.password=ardorpass```

### Running Application in Server

1. To encrypt the DB username and passwords in the server use below command in terminal with profile/env
   ```mvn jasypt:encrypt "-Djasypt.encryptor.password=ardorpass" "-Djasypt.plugin.path=file:src\main\resources\application-<profile/env>.yaml"```
2. Run this command in terminal ```mvn clean install "-Djasypt.encryptor.password=ardorpass"``` to install all the required dependencies.
3. Run the main class with VM arguments ```java -jar .\target\flights-api-0.0.1-SNAPSHOT.jar --jasypt.encryptor.password=ardorpass```
