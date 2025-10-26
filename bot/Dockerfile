FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build.gradle ./
COPY gradlew ./
COPY gradle ./gradle

COPY src ./src

COPY "build/libs/*.jar" "app.jar"

CMD ["java", "-jar", "app.jar"]