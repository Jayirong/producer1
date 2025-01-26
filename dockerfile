FROM openjdk:21-ea-24-oracle

WORKDIR /app

COPY target/producer1-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

CMD [ "java", "-jar", "app.jar" ]