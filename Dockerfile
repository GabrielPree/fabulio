# Estágio 1: Build com Java 25 (Eclipse Temurin)
FROM eclipse-temurin:25-jdk AS build

# Instalar Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Definir diretório de trabalho
WORKDIR /app

# Copiar o pom.xml e baixar dependências (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar o código fonte
COPY src ./src

# Build do projeto com encoding UTF-8
RUN mvn package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# Estágio 2: Runtime (imagem final menor)
FROM eclipse-temurin:25-jdk

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR gerado do estágio de build
COPY --from=build /app/target/Fabulio-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta do Spring Boot
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]