FROM eclipse-temurin:17-jdk-alpine
RUN apk add --no-cache maven
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "target/StatisticsService-0.0.1-SNAPSHOT.jar"]