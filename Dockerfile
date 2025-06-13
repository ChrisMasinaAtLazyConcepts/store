FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/store.jar /app/store.jar

EXPOSE 8080

CMD ["java", "-jar", "store.jar"]