package com.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.features.CallLogging
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level

fun main(args: Array<String>): Unit {
    println("heya")
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging) {
        level = Level.INFO
        // in presence of filters only these get logged
//        filter { call -> call.request.path().startsWith("/section1") }
//        filter { call -> call.request.path().startsWith("/section2") }
        // ...
    }

    routing {
        get("/health") {
            call.respondText("OK")
        }

        get("/client") {
            runBlocking {
                val client = HttpClient(Apache) {
                    install(JsonFeature) {
                        serializer = GsonSerializer {
                            // .GsonBuilder
                            serializeNulls()
                            disableHtmlEscaping()
                        }
                    }
                    install(Logging, fun Logging.Config.() {
                        logger = Logger.DEFAULT
                        level = LogLevel.INFO
                    })
                }

                val message = client.get<String>("http://127.0.0.1:8080/healths")
                println("CLIENT: Message from the server: $message")

                client.close()
            }
            call.respondText("OK")
        }
    }
}
