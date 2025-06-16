FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/store.jar /app/store.jar

EXPOSE 8080

CMD ["java", "-jar", "store.jar"]

# I tied to build the  image using my github workflow andI dont have any Runner configured, all my builds queued