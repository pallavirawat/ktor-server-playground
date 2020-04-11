import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.3.61"
}

group = "com.example"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}


//task generateJks(type: JavaExec, dependsOn: 'classes') {
//    classpath = sourceSets.main.runtimeClasspath
//    main = 'io.ktor.samples.http2.CertificateGenerator'
//}
//is equivalent to
tasks.register<JavaExec>("generateJks"){
    classpath = sourceSets.main.get().runtimeClasspath
    main = "com.example.CertificateGeneratorKt"
}

//getTasksByName("run", false).first().dependsOn('generateJks')
// is equivalent to
tasks.getByName("run"){
    dependsOn("generateJks")
}

//this also works... take all the tasks and do it inside one block
//tasks{
//    "run"{
//        dependsOn("generateJks")
//    }
//}
//


application {
    mainClassName = "ApplicationKt"
}


kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")
