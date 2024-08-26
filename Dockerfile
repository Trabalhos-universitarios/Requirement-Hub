FROM maven:3.9.6-amazoncorretto-17 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install

FROM amazoncorretto:17

COPY --from=build /app/target/requirementhub-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

# docker run -d -p 8080:8080 eliasfernandescout/reqhub:1.0
#docker run -d -p 8080:8080 --name rhb-backend eliasfernandescout/backend-requirementhub:2.0
# docker build --platform linux/amd64 -t eliasfernandescout/backend-requirementhub:1.0 .
# docker pull eliasfernandescout/backend-requirementhub:1.0