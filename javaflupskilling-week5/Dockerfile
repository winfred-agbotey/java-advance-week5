# Build Application
FROM openjdk:21-bullseye AS build

WORKDIR /app

COPY . .

RUN chmod 755 mvnw

# RUN ./mvnw package -DskipTests
RUN ./mvnw package -Dmaven.test.skip

# Serve Application
FROM openjdk:21-bullseye

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

CMD [ "java", "-jar", "app.jar" ]