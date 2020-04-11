package com.example

import io.ktor.network.tls.certificates.generateCertificate
import java.io.File

fun main(args: Array<String>) {
    val jksFile = File("build/temporary.jks").apply {
        parentFile.mkdirs()
    }

    if (!jksFile.exists()) {
        generateCertificate(jksFile) // Generates the certificate
    }
}


