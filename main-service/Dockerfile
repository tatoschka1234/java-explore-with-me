FROM amazoncorretto:21
WORKDIR /app
COPY target/main-service-*.jar main-service.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "main-service.jar"]
