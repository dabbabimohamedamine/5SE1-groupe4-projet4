FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Créer un script pour vérifier si MySQL est prêt
RUN echo '#!/bin/bash \n\
until nc -z mysqldb 3306; do \n\
  echo "Waiting for MySQL..."; \n\
  sleep 5; \n\
done \n\
echo "MySQL is ready!"' > /wait-for-mysql.sh

# Rendre le script exécutable
RUN chmod +x /wait-for-mysql.sh

# Attendre que MySQL soit prêt puis démarrer l'application Spring Boot
ENTRYPOINT ["/wait-for-mysql.sh", "&&", "java", "-jar", "app.jar"]

EXPOSE 8082
