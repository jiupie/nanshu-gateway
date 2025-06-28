FROM eclipse-temurin:11-jdk-ubi9-minimal
WORKDIR /app
ADD ../target/*.jar app.jar
ENV JAVA_OPTS=""
ENV PARAMS="--spring.profiles.active=prod"
ENTRYPOINT [ "sh", "-c", "java  $JAVA_OPTS -jar /app.jar $PARAMS" ]