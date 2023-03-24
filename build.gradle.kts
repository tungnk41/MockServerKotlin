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
val h2_version: String by project

plugins {
    application
    kotlin("jvm") version "1.8.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id("io.ktor.plugin") version "2.2.4"
}

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}

application {
    mainClass.set("com.mock.ApplicationKt")
}

//Change name shadow jar file
ktor {
    fatJar {
        archiveFileName.set("server.jar")
    }
}


dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")

    implementation("com.google.code.gson:gson:$gson_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")

    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")


    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")

    //Websocket
    implementation("io.ktor:ktor-server-websockets:$ktor_version")
//    implementation("io.ktor:ktor-network:$ktor_version")
//    implementation("io.ktor:ktor-network-tls:$ktor_version")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    //Local Database H2
    implementation("com.h2database:h2:$h2_version")

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