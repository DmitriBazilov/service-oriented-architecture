import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.openapi.generator") version "7.8.0"
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

dependencies {
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.wildfly:wildfly-ejb-client-bom:32.0.0.Final")
	implementation(project(":soa-products-ejb"))
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

tasks.withType<KotlinCompile> {
	dependsOn(tasks.openApiGenerate)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sourceSets {
	main {
		kotlin {
			srcDirs("${layout.buildDirectory.get().asFile}/generated/openapi/src/main/kotlin")
		}
	}
}

openApiGenerate {
	generatorName.set("kotlin-spring")
	inputSpec.set("$projectDir/../../../contract/products-api/api.yaml")
	outputDir.set("${layout.buildDirectory.get().asFile}/generated/openapi")
	apiPackage.set("generated.soa.products.controller")
	modelPackage.set("generated.soa.products.dto")
	invokerPackage.set("generated.soa.products")
	configOptions.set(
		mapOf(
			"useSpringBoot3" to "true",
			"dateLibrary" to "java8",
			"generateApis" to "true",
			"generateApiTests" to "false",
			"generateModels" to "true",
			"generateModelTests" to "false",
			"generateModelDocumentation" to "false",
			"generateSupportingFiles" to "false",
			"hideGenerationTimestamp" to "true",
			"interfaceOnly" to "true",
			"library" to "spring-boot",
			"serializableModel" to "true",
			"useBeanValidation" to "true",
			"useTags" to "true",
			"implicitHeaders" to "true",
			"openApiNullable" to "false",
		)
	)
}
