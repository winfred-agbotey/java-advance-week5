# First stage: Build the application using Maven
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
COPY src/main/resources/data.csv .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests
# Second stage: Create a smaller runtime image with JDK
FROM openjdk:17-alpine AS runtime
WORKDIR /app

# Set the necessary Java options to handle permissions
ENV JDK_JAVA_OPTIONS="--add-opens=java.base/java.nio=ALL-UNNAMED \
                      --add-opens=java.base/java.lang.invoke=ALL-UNNAMED \
                      --add-opens=java.base/java.util=ALL-UNNAMED \
                      --add-opens=java.base/sun.nio.ch=ALL-UNNAMED \
                      -Dio.netty.tryReflectionSetAccessible=true"

# Copy the JAR file built in the first stage
COPY --from=build /app/target/RE.jar /app/RE.jar
COPY --from=build /app/data.csv /app/data.csv

# Run the application
ENTRYPOINT ["java", "-jar", "/app/RE.jar"]