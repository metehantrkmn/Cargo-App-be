# Use an Alpine Linux base image
FROM alpine:latest

# Install OpenJDK
RUN apk --no-cache add openjdk21

# Set the JAVA_HOME environment variable
ENV JAVA_HOME /usr/lib/jvm/default-jvm

# Add Java to the PATH
ENV PATH ${PATH}:${JAVA_HOME}/bin

# Your additional Dockerfile instructions go here

# Example: Copy your application JAR into the container
COPY target/Authentication-0.0.1-SNAPSHOT.jar /app/app.jar

# Specify the command to run on container start
CMD ["java", "-jar", "/app/app.jar"]