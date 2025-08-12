package kr.jm.data.repository

import kr.jm.data.datasource.LocalSubwayStationDataSource
import kr.jm.domain.model.SubwayStation
import kr.jm.domain.repository.SubwayStationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubwayStationRepositoryImpl @Inject constructor(
    private val localDataSource: LocalSubwayStationDataSource
) : SubwayStationRepository {

    // 나중에 북마크 상태를 관리할 저장소 (SharedPreferences 등)
    private val bookmarkedStations = mutableSetOf<String>()

    override suspend fun getAllStations(): List<SubwayStation> {
        return localDataSource.getSubwayStations().map { dto ->
            SubwayStation(
                id = dto.notUse,
                name = dto.stationName,
                line = dto.lineName,
                latitude = dto.latitude,
                longitude = dto.longitude,
                isBookmarked = bookmarkedStations.contains(dto.notUse)
            )
        }
    }

    override suspend fun searchStations(query: String): List<SubwayStation> {
        val allStations = getAllStations()
        return if (query.isBlank()) {
            allStations
        } else {
            allStations.filter { station ->
                station.name.contains(query, ignoreCase = true)
            }
        }
    }

    override suspend fun getBookmarkedStations(): List<SubwayStation> {
        return getAllStations().filter { it.isBookmarked }
    }

    override suspend fun bookmarkStation(stationId: String) {
        bookmarkedStations.add(stationId)
    }

    override suspend fun unbookmarkStation(stationId: String) {
        bookmarkedStations.remove(stationId)
    }
}