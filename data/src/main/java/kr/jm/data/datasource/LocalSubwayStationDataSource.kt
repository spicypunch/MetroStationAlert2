package kr.jm.data.datasource

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kr.jm.data.model.SubwayStationDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalSubwayStationDataSource @Inject constructor(
    private val context: Context
) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    private val listType = Types.newParameterizedType(List::class.java, SubwayStationDto::class.java)
    private val adapter: JsonAdapter<List<SubwayStationDto>> = moshi.adapter(listType)
    
    fun getSubwayStations(): List<SubwayStationDto> {
        return try {
            val json = context.assets.open("SubwayStations.json")
                .bufferedReader()
                .use { it.readText() }
            
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}