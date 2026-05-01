plugins {
    kotlin("jvm") version "2.2.0"
    `java-library`
    application
    `maven-publish`
}

group = "io.avasia"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
    withJavadocJar()
}

application {
    mainClass.set("io.avasia.projecttrellis.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "project-trellis"

            pom {
                name.set("ProjectTrellis")
                description.set("A small Kotlin/JVM service for growing project plans into Trello board structure.")
                url.set("https://github.com/avasia/project-trellis")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
            }
        }
    }
}
