package ltd.lulz.service.hello.controller

import ltd.lulz.service.hello.service.HelloService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController(
    val service: HelloService,
) {

    @GetMapping("/")
    suspend fun getHello(): String = service.getGrating()
}
