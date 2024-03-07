package ltd.lulz.service.hello.etity

import java.util.UUID
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table("devices")
data class DeviceEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val device: UUID,
    val data: String? = null,
    val version: Int = 0,
)
