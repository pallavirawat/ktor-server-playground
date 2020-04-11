package com.example

import io.ktor.application.*
import io.ktor.network.tls.certificates.generateCertificate
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.get
import io.ktor.routing.routing
import java.io.File

fun main(args: Array<String>): Unit {
    println("heya")
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/health_check") {
            call.respondText("OK")
        }
    }
}

fun generateCert(){
    val jksFile = File("buildcert/temporary.jks").apply {
        parentFile.mkdirs()
    }

    if (!jksFile.exists()) {
        generateCertificate(jksFile) // Generates the certificate
    }
}
