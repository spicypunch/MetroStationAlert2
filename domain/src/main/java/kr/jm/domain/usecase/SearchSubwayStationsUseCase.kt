package kr.jm.domain.usecase

import kr.jm.domain.model.SubwayStation
import kr.jm.domain.repository.SubwayStationRepository

class SearchSubwayStationsUseCase(
    private val repository: SubwayStationRepository
) {
    suspend operator fun invoke(query: String): List<SubwayStation> {
        return repository.searchStations(query)
            .sortedBy { it.line }
    }
}