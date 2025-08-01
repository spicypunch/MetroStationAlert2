package kr.jm.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubwayStationDto(
    @Json(name = "notUse") val notUse: String,
    @Json(name = "stationName") val stationName: String,
    @Json(name = "lineName") val lineName: String,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "latitude") val latitude: Double
)
