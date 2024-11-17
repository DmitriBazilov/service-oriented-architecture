#!/bin/bash

cp build/libs/main-soa-1-1.0-SNAPSHOT.war wildfly/standalone/deployments

bash wildfly/bin/standalone.sh
