package kr.jm.data.repository

import kotlinx.coroutines.flow.first
import kr.jm.data.datasource.LocalSubwayStationDataSource
import kr.jm.domain.model.SubwayStation
import kr.jm.domain.repository.SubwayStationRepository
import kr.jm.domain.repository.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubwayStationRepositoryImpl @Inject constructor(
    private val localDataSource: LocalSubwayStationDataSource,
    private val userPreferencesRepository: UserPreferencesRepository
) : SubwayStationRepository {

    override suspend fun getAllStations(): List<SubwayStation> {
        val bookmarkedStations = userPreferencesRepository.getBookmarks().first()
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

    override suspend fun searchStations(query: String, filteredStations: List<SubwayStation>): List<SubwayStation> {
        return if (query.isBlank()) {
            filteredStations
        } else {
            filteredStations.filter { station ->
                station.name.contains(query, ignoreCase = true)
            }
        }
    }

    override suspend fun getBookmarkedStations(): List<SubwayStation> {
        return getAllStations().filter { it.isBookmarked }
    }

    override suspend fun bookmarkStation(stationId: String) {
        userPreferencesRepository.addBookmark(stationId)
    }

    override suspend fun unbookmarkStation(stationId: String) {
        userPreferencesRepository.removeBookmark(stationId)
    }
}