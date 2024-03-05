package ltd.lulz.service.hello.service

import ltd.lulz.service.hello.configuration.HelloConfiguration
import org.springframework.stereotype.Service

@Service
class HelloService(
    val configuration: HelloConfiguration,
) {

    fun getGrating(): String = "Hello there, This is ${configuration.name} v${configuration.version}!!!"
}
