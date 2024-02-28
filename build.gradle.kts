import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStartContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStopContainer
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_SRC_DIR_KOTLIN
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_TEST_SRC_DIR_KOTLIN
import java.lang.System.getProperty
import java.time.OffsetDateTime.now
import java.time.ZoneId.of
import java.time.format.DateTimeFormatter.ofPattern
import org.gradle.api.JavaVersion.VERSION_17
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.SARIF

plugins {
    id("com.bmuschko.docker-spring-boot-application") version "9.4.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("org.springframework.boot") version "3.2.3"

    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

description = "Lulz Ltd Test Service Hello"
group = "ltd.lulz.service"

val timestamp = now()
    .atZoneSameInstant(of("UTC"))
    .format(ofPattern("yyyy-MM-dd HH:mm:ss z"))
    .toString()

detekt {
    buildUponDefaultConfig = true
    basePath = projectDir.path
    source.from(DEFAULT_SRC_DIR_KOTLIN, DEFAULT_TEST_SRC_DIR_KOTLIN)
}

java {
    sourceCompatibility = VERSION_17
    targetCompatibility = VERSION_17
}

ktlint {
    verbose = true
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
    kotlinScriptAdditionalPaths {
        include(fileTree("scripts/*"))
    }
    reporters {
        reporter(SARIF)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

tasks {
    register("buildContainer", DockerBuildImage::class) {
        doNotTrackState("disable file checks for this task")
        group = "container"
        inputDir.set(file("./"))
        images.add("${project.name}:${project.version}")
    }
    register("createContainer", DockerCreateContainer::class) {
        group = "container"
        targetImageId("${project.name}:${project.version}")
        containerName.set(project.name)
        hostConfig.portBindings.set(listOf("${project.findProperty("dockerPort") ?: "8080"}:8080"))
        hostConfig.autoRemove.set(true)
        hostConfig.network.set("develop")
        withEnvVar("SPRING_PROFILES_ACTIVE", "docker")
    }
    register("startContainer", DockerStartContainer::class) {
        group = "container"
        dependsOn(findByPath("createContainer"))
        targetContainerId(project.name)
    }
    register("stopContainer", DockerStopContainer::class) {
        group = "container"
        targetContainerId(project.name)
    }
    withType<Detekt> {
        reports {
            html.required = false
            md.required = false
            sarif.required = true
            txt.required = false
            xml.required = false
        }
    }
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }
    withType<ProcessResources> {
        filesMatching("**/application.yml") {
            filter { it.replace("%APP_NAME%", project.name.uppercaseFirstChar()) }
            filter { it.replace("%APP_VERSION%", project.version as String) }
            filter { it.replace("%APP_BUILD_TIME%", timestamp) }
            filter { it.replace("%APP_BUILD_OS_NAME%", getProperty("os.name")) }
            filter { it.replace("%APP_BUILD_OS_VERSION%", getProperty("os.version")) }
        }
        onlyIf { file("src/main/resources/application.yml").exists() }
    }
    withType<Test> {
        useJUnitPlatform()
    }
}
