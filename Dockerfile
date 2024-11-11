# Étape de construction avec Maven
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src

# Compiler et packager l'application
RUN mvn clean package -DskipTests

# Étape de création de l'image finale avec OpenJDK
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copier le JAR généré par Maven
COPY --from=build /app/target/*.jar app.jar

# Exposer le port 8082
EXPOSE 8082

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
