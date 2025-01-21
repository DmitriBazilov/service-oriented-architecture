FROM openjdk:21
COPY build/libs/eureka-server-1.0-SNAPSHOT.jar eureka-server-1.0-SNAPSHOT.jar

ENTRYPOINT ["sh", "-c", "sleep 20 && java -jar eureka-server-1.0-SNAPSHOT.jar"]