plugins {
    kotlin("jvm") version "2.2.0"
    `java-library`
    application
    id("com.vanniktech.maven.publish") version "0.36.0"
}

group = "io.github.avasiaxx"
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

application {
    mainClass.set("io.avasia.projecttrellis.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

mavenPublishing {
    coordinates("io.github.avasiaxx", "project-trellis", "1.0.0")

    publishToMavenCentral()
    signAllPublications()

    pom {
        name.set("ProjectTrellis")
        description.set("A small Kotlin/JVM service for growing project plans into Trello board structure.")
        inceptionYear.set("2026")
        url.set("https://github.com/avasiaxx/project-trellis")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("avasiaxx")
                name.set("avasiaxx")
                url.set("https://github.com/avasiaxx")
            }
        }

        scm {
            url.set("https://github.com/avasiaxx/project-trellis")
            connection.set("scm:git:https://github.com/avasiaxx/project-trellis.git")
            developerConnection.set("scm:git:ssh://git@github.com/avasiaxx/project-trellis.git")
        }
    }
}
