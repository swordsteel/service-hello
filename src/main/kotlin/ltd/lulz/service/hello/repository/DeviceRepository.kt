package ltd.lulz.service.hello.repository

import java.util.UUID
import ltd.lulz.service.hello.etity.DeviceEntity
import org.springframework.stereotype.Repository
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository
import reactor.core.publisher.Mono

@Repository
interface DeviceRepository : ReactiveCassandraRepository<DeviceEntity, UUID> {

    fun findByDevice(device: UUID): Mono<DeviceEntity>
}
