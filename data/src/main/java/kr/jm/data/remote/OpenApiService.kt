package kr.jm.data.remote

import kr.jm.domain.model.SubwayArrivalResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenApiService {
    @GET("6746565a646d696e38304642417a6f/json/realtimeStationArrival/0/100/{stationName}")
    suspend fun getSubwayArrivalTime(
        @Path("stationName") stationName: String
    ): SubwayArrivalResponse

}