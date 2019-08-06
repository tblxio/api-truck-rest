FROM openjdk:12-jdk-alpine
VOLUME /tmp
COPY target/lego-rest.jar app.jar
EXPOSE 8080
EXPOSE 3000
EXPOSE 5555
ENTRYPOINT ["java","-jar","/app.jar"]