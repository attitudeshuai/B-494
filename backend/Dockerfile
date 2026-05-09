# Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY settings.xml .
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -s settings.xml

# Run stage
FROM maven:3.8.5-openjdk-17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Create uploads directory
RUN mkdir uploads
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
