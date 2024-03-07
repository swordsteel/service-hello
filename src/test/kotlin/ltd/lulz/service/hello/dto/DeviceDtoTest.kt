package ltd.lulz.service.hello.dto

import java.util.UUID
import ltd.lulz.service.hello.etity.DeviceEntity
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SoftAssertionsExtension::class)
class DeviceDtoTest {

    companion object {
        const val HEX_DATA: String = "00AA99FF"
        val uuid: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val deviceEntityWith: DeviceEntity = DeviceEntity(uuid, uuid, HEX_DATA, 1)
        val deviceEntityWithout: DeviceEntity = DeviceEntity(uuid, uuid)
    }

    @InjectSoftAssertions
    lateinit var softly: SoftAssertions

    @Test
    fun `mapRegisterResponse should map DeviceEntity to RegisterResponse`() {
        // when
        val result = DeviceDto.mapRegisterResponse(deviceEntityWithout)

        // Assert
        softly.assertThat(result).isNotNull
        softly.assertThat(result.id).isEqualTo(uuid)
        softly.assertThat(result.device).isEqualTo(uuid)
    }

    @Test
    fun `mapDeviceResponse should map DeviceEntity all data to DeviceResponse`() {
        // when
        val result = DeviceDto.mapDeviceResponse(deviceEntityWith)

        // Assert
        softly.assertThat(result).isNotNull
        softly.assertThat(result.id).isEqualTo(uuid)
        softly.assertThat(result.device).isEqualTo(uuid)
        softly.assertThat(result.data).isEqualTo(HEX_DATA)
        softly.assertThat(result.version).isEqualTo(1)
    }

    @Test
    fun `mapDeviceResponse should map DeviceEntity without data to DeviceResponse`() {
        // when
        val result = DeviceDto.mapDeviceResponse(deviceEntityWithout)

        // Assert
        softly.assertThat(result).isNotNull
        softly.assertThat(result.id).isEqualTo(uuid)
        softly.assertThat(result.device).isEqualTo(uuid)
        softly.assertThat(result.data).isBlank
        softly.assertThat(result.version).isEqualTo(0)
    }
}
