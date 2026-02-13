FROM eclipse-temurin:21-jdk-ubi9-minimal as builder
WORKDIR /app
COPY ./target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21-jre-ubi9-minimal
WORKDIR /app
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./
ENV JAVA_OPTS=""
ENV PARAMS="--spring.profiles.active=prod"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher $PARAMS" ]
