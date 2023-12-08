plugins {
    kotlin("jvm") version "1.9.20"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-script-runtime")
    implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    val ktorVersion = "2.3.6"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("com.github.ajalt.mordant:mordant:2.2.0")
    implementation(kotlin("test"))
}

sourceSets {
    main {
        kotlin.srcDir("src")
        resources.srcDir("resources")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
