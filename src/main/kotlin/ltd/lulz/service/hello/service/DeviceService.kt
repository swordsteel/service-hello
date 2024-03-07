package ltd.lulz.service.hello.service

import java.util.UUID
import ltd.lulz.service.hello.etity.DeviceEntity
import ltd.lulz.service.hello.repository.DeviceRepository
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class DeviceService(
    private val repository: DeviceRepository,
) {

    companion object {
        const val DEVICE_EXISTS = "Device %s already exists"
        const val DEVICE_NOT_FOUND = "Device %s do not exists"
        const val VERSION_MISMATCH = "Version mismatch error"
    }

    fun registerDevice(
        device: UUID,
    ): Mono<DeviceEntity> = repository.findByDevice(device)
        .flatMap(::deviceExists)
        .switchIfEmpty(createNewDevice(device))

    fun getDevice(uuid: UUID): Mono<DeviceEntity> = repository.findById(uuid)
        .switchIfEmpty(deviceNotFound(uuid))

    fun updateDevice(uuid: UUID, version: Int, data: String): Mono<DeviceEntity> = repository.findById(uuid)
        .flatMap { processRecord(it, version, data) }
        .switchIfEmpty(deviceNotFound(uuid))

    private fun processRecord(
        it: DeviceEntity,
        version: Int,
        data: String,
    ) = if (it.version < version) {
        repository.save(it.copy(data = data, version = version))
    } else {
        Mono.error(ResponseStatusException(CONFLICT, VERSION_MISMATCH))
    }

    private fun deviceNotFound(
        uuid: UUID,
    ): Mono<DeviceEntity> = Mono.error(ResponseStatusException(NOT_FOUND, DEVICE_NOT_FOUND.format(uuid)))

    private fun deviceExists(
        existingDevice: DeviceEntity,
    ): Mono<DeviceEntity> = Mono.error(ResponseStatusException(CONFLICT, DEVICE_EXISTS.format(existingDevice.device)))

    private fun createNewDevice(
        device: UUID,
    ): Mono<DeviceEntity> = Mono.defer { repository.save(DeviceEntity(device = device)) }
}
