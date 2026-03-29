# Use lightweight Java runtime
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper + config first (for caching)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Give permission to mvnw
RUN chmod +x mvnw

# Download dependencies (faster rebuilds)
RUN ./mvnw dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port (Render uses PORT env variable)
EXPOSE 8080

# Run the app
CMD ["sh", "-c", "java -jar target/*.jar --server.port=${PORT}"]