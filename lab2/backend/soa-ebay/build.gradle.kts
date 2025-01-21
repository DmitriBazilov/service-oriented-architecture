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
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
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
    inputSpec.set("$projectDir/../../contract/ebay-api/api.yaml")
    outputDir.set("${layout.buildDirectory.get().asFile}/generated/openapi")
    apiPackage.set("generated.soa.ebay.controller")
    modelPackage.set("generated.soa.ebay.dto")
    invokerPackage.set("generated.soa.ebay")
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
