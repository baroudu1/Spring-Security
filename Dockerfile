FROM openjdk:17
COPY ./target/Security-JWT-0.0.1-SNAPSHOT.jar Security-JWT-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "Security-JWT-0.0.1-SNAPSHOT.jar"]
