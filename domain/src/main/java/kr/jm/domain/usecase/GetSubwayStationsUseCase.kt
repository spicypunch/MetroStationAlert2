package kr.jm.domain.usecase

import kr.jm.domain.model.SubwayStation
import kr.jm.domain.repository.SubwayStationRepository

class GetSubwayStationsUseCase(
    private val repository: SubwayStationRepository
) {
    suspend operator fun invoke(): List<SubwayStation> {
        return repository.getAllStations()
    }
}