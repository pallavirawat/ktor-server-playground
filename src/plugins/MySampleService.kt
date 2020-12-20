package com.example.plugins

import io.ktor.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlin.system.exitProcess

class MySampleService(monitor: ApplicationEvents) {
    public class Configuration {

    }

    private val starting: (Application) -> Unit = { println("Application starting sns: $it") }
    private val started: (Application) -> Unit = { println("Application started sns: $it") }
    private val stopping: (Application) -> Unit = { println("Application stopping sns: $it") }
    private var stopped: (Application) -> Unit = {}

    init {
        stopped = {
            println("Application stopped sns: $it")
            monitor.unsubscribe(ApplicationStarting, starting)
            monitor.unsubscribe(ApplicationStarted, started)
            monitor.unsubscribe(ApplicationStopping, stopping)
            monitor.unsubscribe(ApplicationStopped, stopped)
        }

        monitor.subscribe(ApplicationStarting, starting)
        monitor.subscribe(ApplicationStarted, started)
        monitor.subscribe(ApplicationStopping, stopping)
        monitor.subscribe(ApplicationStopped, stopped)
    }

    fun hello(){
        println("hello from mah side")
    }

    public companion object Feature : ApplicationFeature<Application, Configuration, MySampleService> {
        override val key: AttributeKey<MySampleService> = AttributeKey("My Service")
        override fun install(pipeline: Application, configure: Configuration.() -> Unit): MySampleService {
            val mahPhase = PipelinePhase("MyService")
            val configuration = Configuration().apply(configure)
            val feature = MySampleService(
                pipeline.environment.monitor
            )

//            pipeline.insertPhaseBefore(ApplicationCallPipeline.Monitoring, mahPhase)
            pipeline.insertPhaseAfter(ApplicationCallPipeline.Setup, mahPhase)

//            exitProcess(0)
            return feature
        }
    }
}
