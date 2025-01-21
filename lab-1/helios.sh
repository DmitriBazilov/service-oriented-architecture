#!/bin/bash

./gradlew clean 
./gradlew war

scp -P 2222 ./main-service/build/libs/main-service-1.0-SNAPSHOT.war s335102@se.ifmo.ru:~/wildfly/standalone/deployments/
scp -P 2222 ./grammy-service/build/libs/grammy-service-1.0-SNAPSHOT.war s335102@se.ifmo.ru:~/wildfly-grammy/standalone/deployments/
