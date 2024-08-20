plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("kotlin-kapt")
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.9.20"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        create<MavenPublication>("mavenKapt") {
            afterEvaluate {
                from(components["kotlin"])

                groupId = "com.github.sitharaj88"
                artifactId = "shared-pref-orm"
                version = "1.0.0"
            }
        }
    }
    repositories {
        mavenLocal() // Publishes to the local .m2 repository
    }
}

dependencies {
    implementation(libs.kotlin.std)
    implementation(libs.kotlin.poet)
    implementation(libs.auto.service)
    kapt(libs.auto.service)
}