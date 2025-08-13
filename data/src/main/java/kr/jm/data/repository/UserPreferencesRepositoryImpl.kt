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

    override suspend fun addBookmark(stationId: String) {
        dataStore.addBookmark(stationId)
    }

    override suspend fun removeBookmark(stationId: String) {
        dataStore.removeBookmark(stationId)
    }

    override fun getBookmarks(): Flow<Set<String>> {
        return dataStore.getBookmarks()
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
}