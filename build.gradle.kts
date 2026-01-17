plugins {
    kotlin("jvm") version "2.2.21"
    `maven-publish`
    signing
}

group = "io.github.xerooup"
version = "0.1.2"

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("xhttp")
                description.set("simple api for sending web requests")
                url.set("https://github.com/xerooup/xhttp")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("xeroup")
                        name.set("xeroup")
                        url.set("https://github.com/xerooup")
                    }
                }

                scm {
                    url.set("https://github.com/xerooup/xhttp")
                    connection.set("scm:git:https://github.com/xerooup/xhttp.git")
                    developerConnection.set("scm:git:ssh://github.com/xerooup/xhttp.git")
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
}