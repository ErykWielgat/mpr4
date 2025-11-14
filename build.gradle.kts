plugins {
    id("java")
    id("application")
    id("jacoco")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.opencsv:opencsv:5.9")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.26.0")
    testImplementation("org.hamcrest:hamcrest:2.2")
}

tasks.test {
    useJUnitPlatform()
}


tasks.jacocoTestReport {
    reports {
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("custom-coverage/html"))

        xml.required.set(true)
        xml.outputLocation.set(layout.buildDirectory.file("custom-coverage/report.xml"))

        csv.required.set(false)
    }
}

application {
    mainClass.set("org.example.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}
