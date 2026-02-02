# ETAPA 1: Construção (Usa a versão MAIS NOVA do Gradle com Java 21)
FROM gradle:jdk21-jammy AS builder
WORKDIR /app
COPY . .
# Constrói o executável (ignora testes para ser rápido)
RUN gradle bootJar -x test --no-daemon

# Comando de segurança: Encontra o JAR certo e renomeia para app.jar
# Isso evita o erro do "plain.jar" que aconteceu antes
RUN find build/libs -name "*.jar" ! -name "*-plain.jar" -exec cp {} build/libs/app.jar \;

# ETAPA 2: Imagem Final (Leve apenas para rodar)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Pega o app.jar que separamos na etapa anterior
COPY --from=builder /app/build/libs/app.jar .
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]