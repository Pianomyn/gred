plugins {
    id("java")
    //id("checkstyle")
    id("checkstyle")
    id("com.diffplug.spotless") version "6.25.0"
}

group = "org.pianomyn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.pianoyn.gred.Main" // Replace com.example.Main with your main class
    }
}

apply {
  plugin("checkstyle")
}

checkstyle {
  toolVersion = "10.14.2"
  config = resources.text.fromFile("config/checkstyle/checkstyle.xml")
}

tasks.named<Checkstyle>("checkstyleMain") {
    group = "lint"
    description = "Check Java code style"
    source("src/main/java/org/pianomyn/gred")
}

tasks.named<Checkstyle>("checkstyleTest") {
    group = "lint"
    description = "Check Java code style"
    source("src/test/java/org/pianomyn/gred/test")
}

spotless {
  java {
    target("**/*.java")
  }
}

tasks.named("spotlessJava") {
  group = "format"
  description = "Format Java code"
}

