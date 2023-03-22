import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgres_version: String by project
val hikaricp_version: String by project
val exposed_version: String by project
val gson_version: String by project
val postgresql_version: String by project
val jbcrypt_version: String by project
val firebase_admin_version: String by project
val commons_email_version: String by project
val koin_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.20"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.mock"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

application {
    mainClass.set("com.mock.ApplicationKt")
    project.setProperty("mainClassName", "com.mock.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "com.mock.ApplicationKt"))
        }
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")

    implementation("com.google.code.gson:gson:$gson_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")

    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")


    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    // Hikari
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.postgresql:postgresql:$postgresql_version")


    // Koin for Kotlin
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    // Password encryption
    implementation("org.mindrot:jbcrypt:$jbcrypt_version")

    // For sending reset-password-mail
    implementation("org.apache.commons:commons-email:$commons_email_version")

    // Firebase admin
    implementation ("com.google.firebase:firebase-admin:$firebase_admin_version")

}