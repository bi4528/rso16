#FROM adoptopenjdk:15-jre-hotspot
FROM eclipse-temurin:17-jre

RUN mkdir /app

WORKDIR /app

ADD ./api/target/seznam-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8081

CMD ["java", "-jar", "seznam-api-1.0.0-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "seznam-api-1.0.0-SNAPSHOT.jar"]
#CMD java -jar seznam-api-1.0.0-SNAPSHOT.jar