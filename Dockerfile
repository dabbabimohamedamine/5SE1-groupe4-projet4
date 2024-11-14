# Étape 1 : Construction de l'image avec Maven
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Construction de l'image Java avec le JDK et installation des outils nécessaires
FROM openjdk:17-jdk-slim
WORKDIR /app

# Installer netcat et mysql-client
RUN apt-get update && apt-get install -y netcat mysql-client

# Copier le fichier jar généré depuis l'étape 1
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
