import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import java.util.UUID
import ltd.lulz.service.hello.etity.DeviceEntity
import ltd.lulz.service.hello.repository.DeviceRepository
import ltd.lulz.service.hello.service.DeviceService
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@Suppress("ReactiveStreamsUnusedPublisher")
@ExtendWith(MockKExtension::class, SoftAssertionsExtension::class)
class DeviceServiceTest {

    companion object {
        const val NULL_UUID = "00000000-0000-0000-0000-000000000000"
        const val ID_UUID = "11111111-1111-1111-1111-111111111111"
        val uuid: UUID = UUID.fromString(NULL_UUID)
        val deviceEntity: DeviceEntity = DeviceEntity(uuid, uuid)
        val deviceEntityOld: DeviceEntity = DeviceEntity(UUID.fromString(ID_UUID), uuid, "00", 1)
    }

    @InjectSoftAssertions
    lateinit var softly: SoftAssertions

    @MockK
    lateinit var repository: DeviceRepository

    lateinit var service: DeviceService

    @BeforeEach
    fun setUp() {
        service = DeviceService(repository)

        every { repository.save(any()) } returns Mono.just(deviceEntity)
    }

    @Test
    fun `registerDevice ok`() {
        // given
        every { repository.findByDevice(any(UUID::class)) } returns Mono.empty()

        // when
        val result = service.registerDevice(uuid)

        // then
        StepVerifier.create(result)
            .assertNext { device ->
                softly.assertThat(device).isNotNull
                softly.assertThat(device.id.toString()).isEqualTo(NULL_UUID)
                softly.assertThat(device.device.toString()).isEqualTo(NULL_UUID)
            }
            .verifyComplete()
    }

    @Test
    fun `registerDevice fail - device already exists`() {
        // given
        every { repository.findByDevice(any(UUID::class)) } returns Mono.just(deviceEntity)

        // when
        val result = service.registerDevice(uuid)

        // then
        StepVerifier.create(result)
            .expectError(ResponseStatusException::class.java)
            .verify()
    }

    @Test
    fun `get device ok`() {
        // given
        every { repository.findById(any(UUID::class)) } returns Mono.just(deviceEntity)

        // when
        val result = service.getDevice(uuid)

        // then
        StepVerifier.create(result)
            .assertNext { device ->
                softly.assertThat(device).isNotNull
                softly.assertThat(device.id.toString()).isEqualTo(NULL_UUID)
                softly.assertThat(device.device.toString()).isEqualTo(NULL_UUID)
            }
            .verifyComplete()
    }

    @Test
    fun `get device fail - not found`() {
        // given
        every { repository.findById(any(UUID::class)) } returns Mono.empty()

        // when
        val result = service.getDevice(uuid)

        // Assert
        StepVerifier.create(result)
            .expectError(ResponseStatusException::class.java)
            .verify()
    }

    @Test
    fun `update device ok`() {
        // given
        val version = 2
        val data = "FF"
        every { repository.findById(uuid) } returns Mono.just(deviceEntityOld)
        every { repository.save(any()) } answers { Mono.just(firstArg()) }

        // when
        val result = service.updateDevice(uuid, version, data)

        // then
        StepVerifier.create(result)
            .assertNext { device ->
                softly.assertThat(device).isNotNull
                softly.assertThat(device.id.toString()).isEqualTo(ID_UUID)
                softly.assertThat(device.device.toString()).isEqualTo(NULL_UUID)
                softly.assertThat(device.data).isEqualTo(data)
                softly.assertThat(device.version).isEqualTo(version)
            }
            .verifyComplete()
    }

    @Test
    fun `update device fail - version`() {
        // given
        val version = 1
        val data = "FF"
        every { repository.findById(uuid) } returns Mono.just(deviceEntityOld)
        every { repository.save(any()) } answers { Mono.just(firstArg()) }

        // when
        val result = service.updateDevice(uuid, version, data)

        // Assert
        StepVerifier.create(result)
            .expectError(ResponseStatusException::class.java)
            .verify()
    }

    @Test
    fun `update device fail - not found`() {
        // given
        val version = 2
        val data = "FF"
        every { repository.findById(uuid) } returns Mono.empty()

        // when
        val result = service.updateDevice(uuid, version, data)

        // Assert
        StepVerifier.create(result)
            .expectError(ResponseStatusException::class.java)
            .verify()
    }
}
