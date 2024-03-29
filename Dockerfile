FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=flights-api-0.0.1-SNAPSHOT.jar
COPY target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
