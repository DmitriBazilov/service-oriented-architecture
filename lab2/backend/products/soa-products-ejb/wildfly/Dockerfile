FROM bitnami/wildfly:32.0.0

COPY ./build/libs/soa-products-ejb-0.0.1-SNAPSHOT.jar /opt/bitnami/wildfly/standalone/deployments/
COPY ./wildfly/standalone.xml /opt/bitnami/wildfly/standalone/configuration/

RUN /opt/bitnami/wildfly/bin/add-user.sh -a -u user -p user
RUN /opt/bitnami/wildfly/bin/add-user.sh -u admin -p admin

CMD ["/opt/bitnami/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
