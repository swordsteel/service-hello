package ltd.lulz.service.hello.service

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class HelloServiceTest(
    @Autowired val service: HelloService,
) {

    @Test
    fun `get grating`() {
        // when
        val response = runBlocking { service.getGrating() }

        // then
        assertEquals("Hello there, This is Test v0.1.2!!!", response)
    }
}
