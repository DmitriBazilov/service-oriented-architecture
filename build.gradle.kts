plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.allopen") version "2.0.20"
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

dependencies {
    compileOnly("jakarta.enterprise:jakarta.enterprise.cdi-api:3.0.0")
    compileOnly("jakarta.ejb:jakarta.ejb-api:4.0.0")
    compileOnly("jakarta.ws.rs:jakarta.ws.rs-api:3.0.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:5.0.0")
    compileOnly("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}