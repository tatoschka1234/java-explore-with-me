FROM amazoncorretto:21
WORKDIR /app
COPY target/main-service-*.jar main-server.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "main-server.jar"]
