plugins {
    id("io.spring.dependency-management") version "1.1.4"
    id("org.springframework.boot") version "3.2.3"

    id("ltd.lulz.plugin.common.service") version "0.2.0"

    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    componentTestImplementation("org.springframework.boot:spring-boot-starter-test")

    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test")
}

description = "Lulz Ltd Test Service Hello"
group = "ltd.lulz.service"
