# Étape 1 : Build
FROM openjdk:17-jdk-slim as build

WORKDIR /app

# Copie du fichier pom.xml et du wrapper Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Rendre mvnw exécutable
RUN chmod +x mvnw

# Télécharger les dépendances
RUN ./mvnw dependency:go-offline -B

# Copie du code source
COPY src src

# Construction de l'application
RUN ./mvnw clean package -DskipTests

# Étape 2 : Runtime
FROM openjdk:17-jdk-slim

WORKDIR /app

# Créer le répertoire de configuration
RUN mkdir -p /app/config

# Copie du JAR depuis l'étape de build
COPY --from=build /app/target/msuser-0.0.1-SNAPSHOT.jar app.jar

# Exposition du port 8080
EXPOSE 8080

# Commande pour lancer l'application avec configuration externe
CMD ["java", "-jar", "app.jar", "--spring.config.location=file:/app/config/application.yml"]