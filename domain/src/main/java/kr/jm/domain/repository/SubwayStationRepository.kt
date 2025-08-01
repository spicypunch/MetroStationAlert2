package kr.jm.domain.repository

import kr.jm.domain.model.SubwayStation

interface SubwayStationRepository {
    suspend fun getAllStations(): List<SubwayStation>
    suspend fun searchStations(query: String): List<SubwayStation>
    suspend fun getBookmarkedStations(): List<SubwayStation>
    suspend fun bookmarkStation(stationId: String)
    suspend fun unbookmarkStation(stationId: String)
}