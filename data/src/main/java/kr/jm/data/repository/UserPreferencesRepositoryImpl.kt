package kr.jm.data.repository

import kotlinx.coroutines.flow.Flow
import kr.jm.data.datastore.UserPreferencesDataStore
import kr.jm.domain.repository.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : UserPreferencesRepository {

    override suspend fun addBookmark(stationName: String): Result<String> {
        return dataStore.addBookmark(stationName)
    }

    override suspend fun removeBookmark(stationName: String): Result<String> {
        return dataStore.removeBookmark(stationName)
    }

    override fun getBookmarks(): Flow<Set<String>> {
        return dataStore.getBookmarks()
    }

    override suspend fun addAlertStationWithLocation(stationName: String, latitude: Double, longitude: Double): Result<String> {
        return try {
            dataStore.addAlertStation(stationName)
            dataStore.saveLocation(latitude, longitude)
            Result.success(stationName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAddedAlertStation(): Flow<String?> {
        return dataStore.getAddedAlertStation()
    }

    override suspend fun addRecentSearch(query: String) {
        dataStore.addRecentSearch(query)
    }

    override fun getRecentSearches(): Flow<List<String>> {
        return dataStore.getRecentSearches()
    }

    override suspend fun clearRecentSearches() {
        dataStore.clearRecentSearches()
    }

    override suspend fun saveDistance(distance: Float): Result<Unit> {
        return try {
            dataStore.saveDistance(distance)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getDistance(): Flow<Float> {
        return dataStore.getDistance()
    }

    override suspend fun saveNotificationSettings(title: String, content: String): Result<Unit> {
        return try {
            dataStore.saveNotificationSettings(title, content)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getNotiTitle(): Flow<String> {
        return dataStore.getNotiTitle()
    }

    override fun getNotiContent(): Flow<String> {
        return dataStore.getNotiContent()
    }

    override suspend fun resetAlertState(): Result<Unit> {
        return dataStore.resetAlertState()
    }

    override fun getAlertState(): Flow<Boolean> {
        return dataStore.getAlertState()
    }

    override suspend fun setAlertState(isActive: Boolean): Result<Unit> {
        return dataStore.setAlertState(isActive)
    }
}