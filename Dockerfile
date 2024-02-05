FROM openjdk:17-jdk-slim AS builder

COPY --from=builder build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]