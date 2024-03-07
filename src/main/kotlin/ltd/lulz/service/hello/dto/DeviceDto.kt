package ltd.lulz.service.hello.dto

import java.util.UUID
import ltd.lulz.service.hello.etity.DeviceEntity

object DeviceDto {

    fun mapRegisterResponse(
        device: DeviceEntity,
    ): RegisterResponse = RegisterResponse(device.id, device.device)

    fun mapDeviceResponse(
        device: DeviceEntity,
    ): DeviceResponse = DeviceResponse(device.id, device.device, device.data ?: "", device.version)

    data class RegisterRequest(
        val device: UUID,
    )

    data class RegisterResponse(
        val id: UUID,
        val device: UUID,
    )

    data class DeviceRequest(
        val data: String,
    )

    data class DeviceResponse(
        val id: UUID,
        val device: UUID,
        val data: String,
        val version: Int,
    )
}
