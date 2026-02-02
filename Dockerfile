# ETAPA 1: Construir o site usando uma imagem que já tem Gradle
FROM gradle:jdk21-alpine AS builder
WORKDIR /app
COPY . .
# Aqui usamos o 'gradle' do sistema, não o arquivo 'gradlew'
RUN gradle clean build -x test --no-daemon

# ETAPA 2: Criar a imagem final leve apenas para rodar
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Pega o arquivo .jar criado na etapa anterior
COPY --from=builder /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]