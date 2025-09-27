package kr.jm.domain.usecase

import kr.jm.domain.repository.LocationRepository
import javax.inject.Inject

class ReactivateAlertUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke() {
        locationRepository.reactivateAlert()
    }
}