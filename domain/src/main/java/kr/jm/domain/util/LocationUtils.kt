package kr.jm.domain.util

import kotlin.math.*

object LocationUtils {
    fun calculateDistance(
        bookmarkLatitude: Double,
        bookmarkLongitude: Double,
        currentLatitude: Double,
        currentLongitude: Double
    ): Double {
        val earthRadius = 6371

        val latDistance = Math.toRadians(currentLatitude - bookmarkLatitude)
        val lonDistance = Math.toRadians(currentLongitude - bookmarkLongitude)

        val a = sin(latDistance / 2).pow(2.0) +
                cos(Math.toRadians(bookmarkLatitude)) *
                cos(Math.toRadians(currentLatitude)) *
                sin(lonDistance / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }
}