FROM openjdk:11-jre-slim

WORKDIR /app
COPY sentinel-dashboard-1.8.6.jar sentinel-dashboard.jar
EXPOSE 8090
ENTRYPOINT ["java", "-Dserver.port=8090", "-Dcsp.sentinel.dashboard.server=localhost:8090", "-Dproject.name=sentinel-dashboard", "-jar", "sentinel-dashboard.jar"]
