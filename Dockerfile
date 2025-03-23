FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/currency-tracker.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]