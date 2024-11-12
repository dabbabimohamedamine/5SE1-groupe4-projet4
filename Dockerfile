# Utilise l'image de base Java
FROM openjdk:17-jdk-slim

# Copie le fichier JAR de Spring Boot dans l'image
COPY target/your-spring-boot-app.jar /app.jar

# Définit le point d'entrée
ENTRYPOINT ["java", "-jar", "/app.jar"]
