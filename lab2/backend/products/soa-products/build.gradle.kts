import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.unbroken-dome.xjc") version "2.0.0"
}

group = "com.soa"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
	}
}

val jacksonVersion = "2.10.5"

dependencies {
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("axis:axis-wsdl4j:1.5.1")

	// Spring Web Services
	implementation("org.springframework.ws:spring-ws-core")

	// JAXB for XML binding
	implementation("javax.xml.bind:jaxb-api:2.3.1")
	implementation("org.glassfish.jaxb:jaxb-runtime:2.3.1")
	implementation("org.jvnet.jaxb2.maven2:maven-jaxb2-plugin:0.14.0")

	xjcTool("com.sun.xml.bind:jaxb-xjc:3.0.0-M4")



	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.wildfly:wildfly-ejb-client-bom:32.0.0.Final")
	implementation(project(":soa-products-ejb"))
}

sourceSets {
	main {
		kotlin {
			xjcTargetPackage = "com.soa.products.generated"
		}
	}
}


configurations.all {
	resolutionStrategy {
		force("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
		force("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
		force("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
		force("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
	}
}

configurations {
	configureEach {
		exclude(group = "ch.qos.logback", module = "logback-classic")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
