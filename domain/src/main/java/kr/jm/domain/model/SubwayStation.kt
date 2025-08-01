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
) : Parcelable {
    
    fun distanceTo(userLatitude: Double, userLongitude: Double): Double {
        val earthRadius = 6371.0 // km
        
        val latDiff = Math.toRadians(userLatitude - latitude)
        val lonDiff = Math.toRadians(userLongitude - longitude)
        
        val a = kotlin.math.sin(latDiff / 2) * kotlin.math.sin(latDiff / 2) +
                kotlin.math.cos(Math.toRadians(latitude)) * kotlin.math.cos(Math.toRadians(userLatitude)) *
                kotlin.math.sin(lonDiff / 2) * kotlin.math.sin(lonDiff / 2)
        
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        
        return earthRadius * c
    }
}