/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn more about Gradle by exploring our samples at https://docs.gradle.org/7.2/samples
 */

plugins {
    id("idea")
    id("java")
    id("application")
    id("com.diffplug.spotless") version "5.14.3"
}

application {
    mainClass.set("me.jiayu.beamjdbcdemo.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

spotless {
    java {
        googleJavaFormat()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.google.auto.value:auto-value-annotations:1.8.2")
    annotationProcessor("com.google.auto.value:auto-value:1.8.2")
    // https://mvnrepository.com/artifact/org.apache.beam/beam-sdks-java-core
    implementation(group = "org.apache.beam", name = "beam-sdks-java-core", version = "2.31.0")
    // https://mvnrepository.com/artifact/org.apache.beam/beam-runners-direct-java
    implementation(group = "org.apache.beam", name = "beam-runners-direct-java", version = "2.31.0")
    // https://mvnrepository.com/artifact/org.apache.beam/beam-sdks-java-io-jdbc
    implementation(group = "org.apache.beam", name = "beam-sdks-java-io-jdbc", version = "2.31.0")
    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    implementation(group = "org.xerial", name = "sqlite-jdbc", version = "3.36.0.1")
}