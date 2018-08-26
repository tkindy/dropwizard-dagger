import groovy.lang.Closure
import org.gradle.kotlin.dsl.support.gradleApiMetadataFrom
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    kotlin("jvm") version "1.2.61"
    id("org.jetbrains.kotlin.kapt") version "1.2.61"
    id("com.github.ben-manes.versions") version "0.20.0"
    id("org.jetbrains.dokka") version "0.9.16"
    `maven-publish`
    id("com.jfrog.bintray") version "1.6"
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

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

bintray {
    user = properties["bintray.user"] as String
    key = properties["bintray.apikey"] as String
    setPublications("mavenJava")

    val versionName = "0.1.0-alpha01"

    pkg = PackageConfig().apply {
        repo = "maven"
        name = "dropwizard-dagger"
        userOrg = user
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/tkindy/dropwizard-dagger.git"
        setLabels("dropwizard", "dagger")
        publicDownloadNumbers = true
        version = VersionConfig().apply {
            name = versionName
            desc = "dropwizard-dagger v$versionName"
            vcsTag = versionName
        }
    }
}

val dokka by tasks.getting(DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(dokka)
}

val sourcesJar by tasks.creating(Jar::class) {
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create("mavenJava", MavenPublication::class.java) {
            artifactId = project.bintray.pkg.name
            from(components["java"])

            artifact(sourcesJar) {
                classifier = "sources"
            }
            artifact(dokkaJar) {
                classifier = "javadoc"
            }
        }
    }
}
