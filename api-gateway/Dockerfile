# Étape 1 : Build
FROM openjdk:17-jdk-slim as build

WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Télécharger les dépendances
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copier le code source
COPY src src

# Construction de l'application
RUN ./mvnw clean package -DskipTests

# Étape 2 : Runtime
FROM openjdk:17-jdk-slim

WORKDIR /app

# Créer le répertoire de configuration
RUN mkdir -p /app/config

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

# Port d'exposition
EXPOSE 8888

# Lancer l'application avec configuration externe
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/config/application.yml"]
