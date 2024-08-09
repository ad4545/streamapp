FROM maven:3.8.1-openjdk-17

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and the source code into the container
COPY pom.xml .
COPY src ./src

RUN mvn clean package
# Copy the built JAR file from the build stage

CMD ["mvn", "exec:java", "-Dexec.mainClass=com.example.SimpleStreamsApp"]


