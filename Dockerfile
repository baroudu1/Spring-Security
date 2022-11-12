FROM openjdk:17
COPY . /opt
WORKDIR /opt
ENTRYPOINT ["java", "-jar", "./target/Security-JWT-0.0.1-SNAPSHOT.jar"]
