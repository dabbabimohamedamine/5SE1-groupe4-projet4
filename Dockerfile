FROM openjdk:17-jdk-alpine
EXPOSE 8082
ADD target/devops-1.0.jar devops-1.0.jar
ENTRYPOINT ["java", "-jar", "/devops-1.0.jar"]
