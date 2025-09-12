package kr.jm.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubwayArrivalResponse(
    val errorMessage: ErrorMessage,
    val realtimeArrivalList: List<RealtimeArrival>
) : Parcelable {
    @Parcelize
    data class ErrorMessage(
        val code: String, // INFO-000
        val developerMessage: String,
        val link: String,
        val message: String, // 정상 처리되었습니다.
        val status: Int, // 200
        val total: Int // 8
    ) : Parcelable

    @Parcelize
    data class RealtimeArrival(
        val arvlCd: String, // 1
        val arvlMsg2: String, // 미금 도착
        val arvlMsg3: String, // 미금
        val barvlDt: String, // 0
        val bstatnId: String, // 13
        val bstatnNm: String, // 신사
        val btrainNo: String, // 4
        val btrainSttus: String, // 일반
        val ordkey: String, // 01000신사0
        val recptnDt: String, // 2024-02-06 14:13:21
        val rowNum: Int, // 1
        val selectedCount: Int, // 4
        val statnFid: String, // 1077006814
        val statnId: String, // 1077006813
        val statnList: String, // 1075075231,1077006813
        val statnNm: String, // 미금
        val statnTid: String, // 1077006812
        val subwayId: String, // 1077
        val subwayList: String, // 1075,1077
        val totalCount: Int, // 8
        val trainLineNm: String, // 신사행 - 정자방면
        val trnsitCo: String, // 2
        val updnLine: String // 상행
    ) : Parcelable
}