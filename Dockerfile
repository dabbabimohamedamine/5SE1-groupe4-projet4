# Étape 1: Utiliser une image Java officielle
FROM openjdk:17-jdk-slim as builder

# Ajouter le code source de l'application dans l'image
WORKDIR /app
COPY ./target/springboot-app.jar springboot-app.jar

# Étape 2: Exposer le port de l'application
EXPOSE 8082

# Exécuter l'application Spring Boot
ENTRYPOINT ["java", "-jar", "springboot-app.jar"]
