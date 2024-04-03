# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-oracle

# Set the working directory in the container
WORKDIR ./

# Copy the application JAR file into the container at /usr/src/app
COPY ./build/libs/gred-1.0-SNAPSHOT.jar ./

# Run the application when the container launches
ENTRYPOINT ["java", "-jar", "gred-1.0-SNAPSHOT.jar"]
