FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy the .jar file into the container
COPY target/store.jar /app/store.jar

# Expose the port that the application will use
EXPOSE 8080

# Run the command to start the application when the container launches
CMD ["java", "-jar", "store.jar"]