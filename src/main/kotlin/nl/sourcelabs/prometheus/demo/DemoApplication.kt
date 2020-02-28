package nl.sourcelabs.prometheus.demo

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@SpringBootApplication
class DemoApplication

class HelloHandler(meterRegistry: MeterRegistry) {
    private val counter = Counter.builder("helloworld").register(meterRegistry)

    fun hello(serverRequest: ServerRequest): ServerResponse {
        counter.increment()
        return ServerResponse.ok().body("Hello world")
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args) {
        addInitializers(
                beans {
                    bean<HelloHandler>()
                    bean {
                        router {
                            GET("/hello", ref<HelloHandler>()::hello)
                        }
                    }
                }
        )
    }
}
