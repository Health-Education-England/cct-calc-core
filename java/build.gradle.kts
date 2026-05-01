plugins {
    java
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "uk.nhs.tis"
version =  findProperty("version") as String? ?: "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.19.4")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

// Copy shared test fixtures to test classpath
tasks.processTestResources {
    from("../fixtures") {
        include("calculation-test-cases.json")
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Health-Education-England/cct-calc-core")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            artifactId = "cct-calc-core"
        }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(artifactId = "cct-calc-core")

    pom {
        name = "CCT Calculation Core"
        description = "Shared CCT calculation engine driven by a JSON rule definition"
        url = "https://github.com/Health-Education-England/cct-calc-core"

        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/license/mit"
            }
        }

        developers {
            developer {
                name = "NHS England"
            }
        }

        scm {
            url = "https://github.com/Health-Education-England/cct-calc-core"
            connection.set("scm:git:git://github.com/Health-Education-England/cct-calc-core.git")
            developerConnection.set("scm:git:ssh://git@github.com/Health-Education-England/cct-calc-core.git")
        }
    }
}

