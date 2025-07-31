FROM amazoncorretto:21
WORKDIR /app
COPY target/stat-server-*.jar stat-server.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "stat-server.jar"]
