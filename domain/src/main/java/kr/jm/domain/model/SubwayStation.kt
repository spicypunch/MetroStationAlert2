package kr.jm.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubwayStation(
    val id: String,
    val name: String,
    val line: String,
    val latitude: Double,
    val longitude: Double,
    val isBookmarked: Boolean = false
) : Parcelable