FROM openjdk:21
COPY build/libs/api-gateway-1.0-SNAPSHOT.jar api-gateway-1.0-SNAPSHOT.jar

ENTRYPOINT ["sh", "-c", "sleep 40 && java -jar api-gateway-1.0-SNAPSHOT.jar"]