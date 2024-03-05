package ltd.lulz.service.hello.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity

@ExtendWith
@SpringBootTest(webEnvironment = RANDOM_PORT)
class HelloServiceTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `get grating`() {
        // when
        val actualResponse: ResponseEntity<String> = restTemplate.getForEntity("/", String::class.java)

        // then
        assertEquals("Hello there, This is Test v0.1.2!!!", actualResponse.body!!)
    }
}
