FROM openjdk:17-jdk-alpine
COPY target/butlleti-0.0.1-SNAPSHOT-jar-with-dependencies.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]