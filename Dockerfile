# ----------------------------
# Stage 1: Build da aplicação
# ----------------------------
FROM maven:3.9.2-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar pom.xml e baixar dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código-fonte e front-end
COPY src ./src

# Build do projeto (gera o jar)
RUN mvn clean package -DskipTests

# ----------------------------
# Stage 2: Imagem final
# ----------------------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar o jar do stage de build
COPY --from=build /app/target/mini-ecommerce-1.0.0.jar app.jar

# Expor porta do Spring Boot
EXPOSE 8080

# Volume para persistir o banco SQLite
VOLUME /app/data

# Rodar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
