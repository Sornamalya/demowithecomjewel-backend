# Use a Java 17 JDK base image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory inside the container
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Build the project using Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Copy the generated JAR to a known location
RUN cp target/demowithecomjewel-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java","-jar","app.jar"]
