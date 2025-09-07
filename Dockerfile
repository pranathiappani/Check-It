# Use an OpenJDK 20 base image 
FROM openjdk:20-jdk-slim 

# Set the working directory inside the container 
WORKDIR /app 

# Copy your compiled Spring Boot jar into the container 
COPY target/*.jar app.jar 

# Expose port 8080 (default Spring Boot port) 
EXPOSE 8080 

# Run the application when the container starts 
ENTRYPOINT ["java", "-jar", "app.jar"]