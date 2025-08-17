package kr.jm.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubwayStation(
    val notUse: String,
    val stationName: String,
    val lineName: String,
    val latitude: Double,
    val longitude: Double,
    val isBookmark: Boolean = false
) : Parcelable