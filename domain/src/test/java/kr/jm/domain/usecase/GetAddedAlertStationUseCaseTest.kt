package kr.jm.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kr.jm.domain.repository.UserPreferencesRepository
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetAddedAlertStationUseCaseTest {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var getAddedAlertStationUseCase: GetAddedAlertStationUseCase

    @Before
    fun setUp() {
        userPreferencesRepository = mockk()
        getAddedAlertStationUseCase = GetAddedAlertStationUseCase(userPreferencesRepository)
    }

    @Test
    fun `알림으로 등록된 역 이름을 Flow로 반환해야 한다`() = runTest {
        // Given (준비)
        val expectedStationName = "야탑역"
        // Repository가 반환할 것으로 예상되는 Flow를 미리 정의합니다.
        val expectedFlow = flowOf(expectedStationName)

        // every를 사용합니다 (suspend 함수가 아니므로 coEvery가 아님).
        // Repository의 getAddedAlertStation() 함수가 호출되면, 위에서 만든 expectedFlow를 반환하도록 설정합니다.
        every { userPreferencesRepository.getAddedAlertStation() } returns expectedFlow

        // When (실행)
        // UseCase를 실행하면 Flow 객체가 반환됩니다.
        val resultFlow = getAddedAlertStationUseCase()

        // Then (검증)
        // 반환된 Flow에서 첫 번째 데이터를 수집(collect)하여 값을 확인합니다.
        // .first()는 Flow에서 첫 번째 값을 꺼내는 suspend 함수입니다.
        assertEquals(expectedStationName, resultFlow.first())

        // verify를 사용합니다 (suspend 함수가 아니므로 coVerify가 아님).
        verify(exactly = 1) { userPreferencesRepository.getAddedAlertStation() }
    }

    @Test
    fun `알림으로 등록된 역이 없으면 null을 포함한 Flow를 반환해야 한다`() = runTest {
        // Given (준비)
        val expectedFlow = flowOf<String?>(null)
        every { userPreferencesRepository.getAddedAlertStation() } returns expectedFlow

        // When (실행)
        val resultFlow = getAddedAlertStationUseCase()

        // Then (검증)
        assertEquals(null, resultFlow.first())
        verify(exactly = 1) { userPreferencesRepository.getAddedAlertStation() }
    }
}
