import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.internal.config.LanguageFeature

plugins {
    kotlin("jvm") version "2.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    val ktorVersion = "3.0.1"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("com.github.ajalt.mordant:mordant:2.7.0")
    implementation(kotlin("test"))
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir("src")
            resources.srcDir("resources")
        }
        all {
            languageSettings {
                languageVersion = KotlinVersion.KOTLIN_2_2.version
                apiVersion = KotlinVersion.KOTLIN_2_2.version

                enableLanguageFeature(LanguageFeature.WhenGuards.name)
                enableLanguageFeature(LanguageFeature.MultiDollarInterpolation.name)
                enableLanguageFeature(LanguageFeature.BreakContinueInInlineLambdas.name)
            }
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}
