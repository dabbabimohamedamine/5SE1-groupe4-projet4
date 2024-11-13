# Étape de construction avec Maven
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

# Copie du fichier pom.xml et récupération des dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copie du code source
COPY src ./src

# Construction du package avec Maven sans les tests
RUN mvn clean package -DskipTests

# Étape de production avec OpenJDK
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copie du JAR généré depuis l'étape de construction
COPY --from=build /app/target/*.jar app.jar

# Exposition du port pour l'application Spring Boot
EXPOSE 8082

# Commande pour exécuter l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
