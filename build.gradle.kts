import org.gradle.kotlin.dsl.support.gradleApiMetadataFrom
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.61"
    id("org.jetbrains.kotlin.kapt").version("1.2.61")
    id("com.github.ben-manes.versions").version("0.20.0")
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.2.61"))
    implementation("io.dropwizard:dropwizard-core:1.3.5")
    implementation("com.google.dagger:dagger:2.17")

    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.11.0")
    testImplementation("org.mockito:mockito-core:2.21.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
    kaptTest("com.google.dagger:dagger-compiler:2.17")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
