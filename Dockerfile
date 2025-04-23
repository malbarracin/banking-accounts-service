# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project's JAR file into the container
COPY target/banking-accounts-service-1.0.0.jar app.jar

# Expose the port the app runs on
EXPOSE 8082

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]