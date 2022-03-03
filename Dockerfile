FROM openjdk:8
EXPOSE 8084
ADD target/sun-travels-api.jar sun-travels-api.jar
ENTRYPOINT ["java","-jar","/sun-travels-api.jar"]