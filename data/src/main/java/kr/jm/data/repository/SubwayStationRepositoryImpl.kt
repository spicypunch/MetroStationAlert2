package kr.jm.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getAllStations(): Flow<List<SubwayStation>> {
        val stations = localDataSource.getSubwayStations()
        return userPreferencesRepository.getBookmarks().map { bookmarkedStations ->
            stations.map { dto ->
                SubwayStation(
                    notUse = dto.notUse,
                    stationName = dto.stationName,
                    lineName = dto.lineName,
                    latitude = dto.latitude,
                    longitude = dto.longitude,
                    isBookmark = bookmarkedStations.contains("${dto.stationName}_${dto.lineName}")
                )
            }
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
