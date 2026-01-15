plugins {
    kotlin("jvm") version "2.2.21"
}

group = "org.xeroup.xhttp"
version = "0.1.2"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}