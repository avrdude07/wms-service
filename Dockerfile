### BUILD STAGE ###
FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

### RUNTIME STAGE ###
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
