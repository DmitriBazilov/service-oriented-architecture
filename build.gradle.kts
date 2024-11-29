plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.allopen") version "2.0.20"
    kotlin("plugin.noarg") version "2.0.20"
    java
    war
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "soa.myts.bazilov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.9.1"

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
}

noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.xml.bind.annotation.XmlRootElement")
}

dependencies {
    compileOnly("jakarta.enterprise:jakarta.enterprise.cdi-api:3.0.0")
    compileOnly("jakarta.ejb:jakarta.ejb-api:4.0.0")
    compileOnly("jakarta.ws.rs:jakarta.ws.rs-api:3.0.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:5.0.0")
    compileOnly("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")

    implementation("org.hibernate:hibernate-core:6.0.2.Final")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.glassfish.jaxb:jaxb-runtime:3.0.2")
    implementation("org.hibernate.validator:hibernate-validator:7.0.4.Final")
    implementation("org.hibernate:hibernate-validator-annotation-processor:7.0.4.Final")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}