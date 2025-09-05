# Use OpenJDK 21 base image
FROM eclipse-temurin:21-jdk AS build

# Set working directory inside container
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (better caching)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Use a smaller runtime image
FROM eclipse-temurin:21-jdk AS runtime

WORKDIR /app

# Copy jar file from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (for Render to map)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]
