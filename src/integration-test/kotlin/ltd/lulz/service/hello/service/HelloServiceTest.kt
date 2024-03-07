package ltd.lulz.service.hello.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith
@SpringBootTest(webEnvironment = RANDOM_PORT)
class HelloServiceTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `get grating`() {
        // when
        val actualResponse = webTestClient.get()
            .uri("/")
            .accept(APPLICATION_JSON)
            .exchange()

        // then
        actualResponse
            .expectStatus().isOk
            .expectBody(String::class.java)
            .consumeWith { response ->
                assertEquals("Hello there, This is Test v0.1.2!!!", response.responseBody)
            }
    }
}
