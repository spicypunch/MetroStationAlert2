package kr.jm.domain.model

data class BookmarkKey(val stationName: String, val lineName: String) {
    companion object {
        fun parse(key: String): BookmarkKey {
            return if (key.contains("_")) {
                val parts = key.split("_")
                BookmarkKey(parts[0], parts[1])
            } else {
                // 마이그레이션을 위한 기존 포맷 처리
                BookmarkKey(key, "전체")
            }
        }
    }
    fun toKey() = "${stationName}_${lineName}"
}
