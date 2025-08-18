package kr.jm.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.model.SubwayStation
import kr.jm.domain.repository.SubwayStationRepository

class GetSubwayStationsUseCase(
    private val repository: SubwayStationRepository
) {
    operator fun invoke(): Flow<List<SubwayStation>> = repository.getAllStations()
}
