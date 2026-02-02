FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test
CMD ["sh", "-c", "java -jar build/libs/*.jar"]