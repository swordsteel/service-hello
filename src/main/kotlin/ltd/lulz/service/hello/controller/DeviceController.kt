package ltd.lulz.service.hello.controller

import java.util.UUID
import ltd.lulz.service.hello.dto.DeviceDto.DeviceRequest
import ltd.lulz.service.hello.dto.DeviceDto.DeviceResponse
import ltd.lulz.service.hello.dto.DeviceDto.RegisterRequest
import ltd.lulz.service.hello.dto.DeviceDto.RegisterResponse
import ltd.lulz.service.hello.dto.DeviceDto.mapDeviceResponse
import ltd.lulz.service.hello.dto.DeviceDto.mapRegisterResponse
import ltd.lulz.service.hello.service.DeviceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/device")
class DeviceController(
    private val service: DeviceService,
) {

    @PostMapping
    fun registerDevice(
        @RequestBody register: RegisterRequest,
    ): Mono<RegisterResponse> = service.registerDevice(register.device).map(::mapRegisterResponse)

    @GetMapping("/{uuid}")
    fun getDevice(
        @PathVariable uuid: UUID,
    ): Mono<DeviceResponse> = service.getDevice(uuid).map(::mapDeviceResponse)

    @PutMapping("/{uuid}")
    fun updateDevice(
        @PathVariable uuid: UUID,
        @RequestParam(required = false) version: Int?,
        @RequestBody deviceRequest: DeviceRequest,
    ): Mono<DeviceResponse> {
        return service.updateDevice(uuid, version ?: -1, deviceRequest.data).map(::mapDeviceResponse)
    }
}
