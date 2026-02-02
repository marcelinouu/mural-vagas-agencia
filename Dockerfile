# Usa uma base leve do Java 21
FROM eclipse-temurin:21-jdk-alpine

# Cria uma pasta para o app
WORKDIR /app

# Copia os arquivos do projeto para dentro do Render
COPY . .

# Dá permissão para o construtor rodar
RUN chmod +x gradlew

# Constrói o site (cria o arquivo .jar)
RUN ./gradlew clean build -x test

# Comando para iniciar o site (procura qualquer arquivo .jar gerado)
CMD ["sh", "-c", "java -jar build/libs/*.jar"]