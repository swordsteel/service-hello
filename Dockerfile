FROM eclipse-temurin:17 as builder
WORKDIR tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
WORKDIR lulz
COPY --from=builder tmp/dependencies/ ./
COPY --from=builder tmp/snapshot-dependencies/ ./
COPY --from=builder tmp/spring-boot-loader/ ./
COPY --from=builder tmp/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
