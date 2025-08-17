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
                notUse = dto.notUse,
                stationName = dto.stationName,
                lineName = dto.lineName,
                latitude = dto.latitude,
                longitude = dto.longitude,
                isBookmark = bookmarkedStations.contains(dto.stationName)
            )
        }
    }

    override suspend fun searchStations(query: String, filteredStations: List<SubwayStation>): List<SubwayStation> {
        return if (query.isBlank()) {
            filteredStations
        } else {
            filteredStations.filter { station ->
                station.stationName.contains(query, ignoreCase = true)
            }
        }
    }
}