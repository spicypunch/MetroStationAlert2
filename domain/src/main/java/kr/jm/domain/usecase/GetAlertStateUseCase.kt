package kr.jm.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.repository.LocationRepository
import javax.inject.Inject

class GetAlertStateUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return locationRepository.getAlertState()
    }
}