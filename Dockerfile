# ETAPA 1: Construção (Usando Ubuntu/Jammy que é mais compatível)
FROM gradle:8.5-jdk21-jammy AS builder
WORKDIR /app
COPY . .
# O comando abaixo constrói o jar ignorando testes para ser mais rápido e seguro
RUN gradle bootJar -x test --no-daemon

# ETAPA 2: Imagem Final (Leve apenas para rodar)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia o arquivo .jar gerado na etapa anterior
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]