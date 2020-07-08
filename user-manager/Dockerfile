FROM adoptopenjdk/openjdk11-openj9:alpine-jre
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.kafka.bootstrap-servers=kafka:9092"]
EXPOSE 9080