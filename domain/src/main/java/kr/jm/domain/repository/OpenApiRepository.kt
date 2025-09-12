package kr.jm.domain.repository

import kr.jm.domain.model.SubwayArrivalResponse

interface OpenApiRepository {
    suspend fun getSubwayArrivalTime(stationName: String): SubwayArrivalResponse
}