package kr.jm.data.repository

import kr.jm.data.remote.OpenApiService
import kr.jm.domain.model.SubwayArrivalResponse
import kr.jm.domain.repository.OpenApiRepository
import javax.inject.Inject

class OpenApiRepositoryImpl @Inject constructor(
    private val openApiService: OpenApiService
) : OpenApiRepository {
    override suspend fun getSubwayArrivalTime(stationName: String): SubwayArrivalResponse {
        return openApiService.getSubwayArrivalTime(stationName)
    }
}