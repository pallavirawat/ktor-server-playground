package com.example

import com.example.plugins.MySampleService
import io.ktor.application.*
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
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.event.Level
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    println("heya")
//    io.ktor.server.netty.EngineMain.main(args)
     CustomEngine.main(args)

//    val server = embeddedServer(Netty, port = 8080){
//        module()
//    }.start(false)
//    Runtime.getRuntime().addShutdownHook(Thread {
//        println("heylooooo shutdown hook adding")
//        server.stop(1, 5, TimeUnit.SECONDS)
//    })
//    Thread.currentThread().join()
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    environment.monitor.subscribe(ApplicationStarted){
        println("My app is ready to roll")
    }
    environment.monitor.subscribe(ApplicationStopped){
        runBlocking {
            println("delaying the stop")
//            delay(10000L)
        }
        println("Time to clean up")
    }
    install(CallLogging) {
        level = Level.INFO
        // in presence of filters only these get logged
//        filter { call -> call.request.path().startsWith("/section1") }
//        filter { call -> call.request.path().startsWith("/section2") }
        // ...
    }

    install(MySampleService){

    }

    install(ShutDownUrl.ApplicationCallFeature) {
        // The URL that will be intercepted
        shutDownUrl = "/ktor/application/shutdown"
        // A function that will be executed to get the exit code of the process
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }

    routing {
        get("/health") {
            call.respondText("OK")
        }

        get("/kill") {
            exitProcess(0)
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
