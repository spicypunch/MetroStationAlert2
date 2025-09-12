package kr.jm.domain.usecase

import kr.jm.domain.model.SubwayArrivalResponse
import kr.jm.domain.repository.OpenApiRepository

class GetSubwayArrivalTimeUseCase(
    private val repository: OpenApiRepository
) {
    suspend operator fun invoke(stationName: String): SubwayArrivalResponse {
        return repository.getSubwayArrivalTime(stationName)
    }
}