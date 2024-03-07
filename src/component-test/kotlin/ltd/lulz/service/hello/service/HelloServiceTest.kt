package ltd.lulz.service.hello.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
@SpringBootTest
class HelloServiceTest(
    @Autowired val service: HelloService,
) {

    @Test
    fun `get grating`() {
        // when
        val process: Mono<String> = service.getGrating()

        // then
        StepVerifier.create(process)
            .expectNext("Hello there, This is Test v0.1.2!!!")
            .verifyComplete()
    }
}
