package ltd.lulz.service.hello.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.application")
data class HelloConfiguration(
    var name: String = "",
    var version: String = "",
)
