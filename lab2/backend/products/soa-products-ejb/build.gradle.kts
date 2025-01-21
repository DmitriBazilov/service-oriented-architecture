plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.allopen") version "2.0.20"
    kotlin("plugin.noarg") version "2.0.20"
    java
    war
}

group = "com.soa"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {

    }

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all of the dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}


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
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("jakarta.ejb:jakarta.ejb-api:4.0.1")
    implementation("jakarta.annotation:jakarta.annotation-api:2.0.0")
    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:3.0.0")
    implementation("org.jboss.ejb3:jboss-ejb3-ext-api:2.4.0.Final")
    testImplementation(kotlin("test"))

    compileOnly("jakarta.ws.rs:jakarta.ws.rs-api:3.0.0")
    compileOnly("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")

    implementation("org.hibernate:hibernate-jpamodelgen:5.0.7.Final")
    implementation("org.hibernate:hibernate-core:6.0.2.Final")
    implementation("org.hibernate.validator:hibernate-validator:7.0.4.Final")
    implementation("org.hibernate:hibernate-validator-annotation-processor:7.0.4.Final")

    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

