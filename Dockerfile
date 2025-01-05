# Use an Alpine-based image with OpenJDK
FROM openjdk:17-alpine

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . .

# Run the Gradle build command
RUN ./gradlew build

# Specify the command to run the application
CMD ["java", "-jar", "server/build/libs/stealth-api-0.2.0.jar"]
