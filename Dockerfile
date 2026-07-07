# Estágio 1: Build
FROM openjdk:25-jdk-slim AS build

# Instalar Maven (já que não temos o mvnw com permissão garantida)
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Definir diretório de trabalho
WORKDIR /app

# Copiar o pom.xml e baixar dependências (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar o código fonte
COPY src ./src

# Build do projeto (pular testes para agilizar)
RUN mvn package -DskipTests

# Estágio 2: Runtime (imagem final menor)
FROM openjdk:25-jdk-slim

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR gerado do estágio de build
COPY --from=build /app/target/Fabulio-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]
