# Use Maven image with JDK 17
FROM maven:3.9.4-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Set environment variables
ENV MAVEN_OPTS="-Xmx1024m"
ENV JAVA_OPTS="-Xmx1024m"

# Copy Maven configuration files
COPY pom.xml .
COPY testng.xml .

# Copy source code
COPY src ./src

# Create necessary directories
RUN mkdir -p target/logs target/screenshots target/allure-results

# Download dependencies (cache layer)
RUN mvn dependency:go-offline -B

# Set permissions
RUN chmod +x /usr/share/maven/bin/mvn

# Default command - can be overridden
CMD ["mvn", "clean", "test", "-Dallure.results.directory=/app/target/allure-results"] 