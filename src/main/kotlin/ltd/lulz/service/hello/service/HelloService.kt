package ltd.lulz.service.hello.service

import ltd.lulz.service.hello.configuration.HelloConfiguration
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class HelloService(
    val configuration: HelloConfiguration,
) {

    fun getGrating(): Mono<String> = Mono.just(
        "Hello there, This is ${configuration.name} v${configuration.version}!!!"
    )
}
