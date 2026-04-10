FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
RUN chmod +x mvnw
COPY src ./src
RUN ./mvnw clean package -DskipTests
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "target/*.jar"]