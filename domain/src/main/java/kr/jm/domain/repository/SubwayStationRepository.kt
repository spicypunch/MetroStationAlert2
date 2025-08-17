package kr.jm.domain.repository

import kr.jm.domain.model.SubwayStation

interface SubwayStationRepository {
    suspend fun getAllStations(): List<SubwayStation>
    suspend fun searchStations(query: String, filteredStations: List<SubwayStation>): List<SubwayStation>
}