package kr.jm.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.model.SubwayStation

interface SubwayStationRepository {
    fun getAllStations(): Flow<List<SubwayStation>>
    suspend fun searchStations(query: String, filteredStations: List<SubwayStation>): List<SubwayStation>
}
