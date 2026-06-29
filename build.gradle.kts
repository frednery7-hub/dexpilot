plugins {
    kotlin("jvm") version "2.3.21"
    application
}

group = "com.dexpilot"
version = "0.1.0-SNAPSHOT"

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.dexpilot.presentation.cli.MainKt")
}

dependencies {
    implementation("info.picocli:picocli:4.7.7")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
}

tasks.test {
    useJUnitPlatform()
}
