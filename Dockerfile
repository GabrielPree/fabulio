# Estágio 1: Build com Java 25 (Eclipse Temurin)
FROM eclipse-temurin:25-jdk AS build

# Instalar Maven e definir encoding
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Definir variáveis de ambiente para encoding
ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en
ENV LC_ALL=en_US.UTF-8

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

# Build com encoding UTF-8
RUN mvn package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# Estágio 2: Runtime
FROM eclipse-temurin:25-jdk

ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en
ENV LC_ALL=en_US.UTF-8

WORKDIR /app
COPY --from=build /app/target/Fabulio-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]