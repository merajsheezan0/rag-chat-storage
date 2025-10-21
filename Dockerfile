# Use official Java 21 image
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven build artifact
COPY rag-chat-storage/target/rag-chat-storage-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java","-jar","app.jar"]
