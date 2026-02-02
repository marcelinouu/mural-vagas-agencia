# ETAPA 1: Construção (Usando imagem oficial Gradle+Ubuntu)
FROM gradle:8.5-jdk21-jammy AS builder
WORKDIR /app
COPY . .
# Roda apenas o bootJar para garantir que cria o executável
RUN gradle bootJar -x test --no-daemon

# Comando mágico: Encontra o JAR certo (que NÃO termina em -plain.jar) e renomeia para app.jar
RUN find build/libs -name "*.jar" ! -name "*-plain.jar" -exec cp {} build/libs/app.jar \;

# ETAPA 2: Imagem Final (Leve apenas para rodar)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Pega apenas o app.jar limpinho da etapa anterior
COPY --from=builder /app/build/libs/app.jar .
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]