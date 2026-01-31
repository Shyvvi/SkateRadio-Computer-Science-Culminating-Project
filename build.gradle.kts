plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "net.shyvv"
version = "0.5"

repositories {
    mavenCentral()
}

javafx {
    version = "25.0.2"
    modules("javafx.controls", "javafx.media")
}

application {
    mainClass.set("net.shyvv.Main")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}