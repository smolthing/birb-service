FROM --platform=linux/amd64 openjdk:17-jdk-alpine
COPY build/libs/starter-1.0.0-SNAPSHOT-fat.jar /app/birb-service.jar
EXPOSE 9000
CMD ["java", "-jar", "/app/birb-service.jar"]
