package ltd.lulz.service.hello.service

import ltd.lulz.service.hello.configuration.HelloConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HelloServiceTest {

    var service: HelloService = HelloService(HelloConfiguration("Test", "0.1.2"))

    @Test
    fun `get grating`() {
        // when
        val response: String = service.getGrating()

        // then
        assertEquals("Hello there, This is Test v0.1.2!!!", response)
    }
}
