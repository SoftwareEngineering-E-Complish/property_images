# Use openjdk for base image
FROM openjdk:17-jdk-slim

# Set the current working directory inside the docker image
WORKDIR /app

# Copy gradlew and gradle folder to the docker image
COPY gradlew .
COPY gradle gradle

# Give execute permission to 'gradlew'
RUN chmod +x ./gradlew

# Copy the build file and the source code to the docker image
COPY build.gradle .
COPY src src
COPY settings.gradle .

# Run the build inside the docker image
RUN ./gradlew build

# Copy the built jar file from build/libs to the root of docker image
RUN cp build/libs/property_images-0.0.1-SNAPSHOT.jar app.jar
# Command to run the spring boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
