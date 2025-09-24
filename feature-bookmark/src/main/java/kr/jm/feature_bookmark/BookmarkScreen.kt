package kr.jm.feature_bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.jm.common_ui.component.CommonStationCard

@Composable
fun BookmarkScreen(
    bookmarkViewModel: BookmarkViewModel = hiltViewModel()
) {
    val bookmarkScreenState by bookmarkViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "지하철 도착 정보🚊",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { bookmarkViewModel.refreshArrivalInfo() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "새로고침",
                    tint = if (bookmarkScreenState.isLoading) Color.Black else Color.Gray
                )
            }
        }
        LazyColumn {
            val stations = bookmarkScreenState.bookmarks.toList()
            items(items = stations, key = { it }) { station ->
                val response = bookmarkScreenState.arrivalTimeMap[station]

                if (response != null) {
                    val directionInfos = bookmarkViewModel.processDirectionArrivalInfo(response)

                    // 축소 상태에서 보여줄 요약 정보 생성
                    val summaryText = if (directionInfos.isNotEmpty()) {
                        directionInfos.joinToString("\n") { directionInfo ->
                            val firstArrival = directionInfo.arrivals.firstOrNull()?.message ?: "정보 없음"
                            "${directionInfo.direction} - $firstArrival"
                        }
                    } else {
                        "도착 정보 없음"
                    }

                    CommonStationCard(
                        primaryText = station,
                        secondText = summaryText,
                        expandableIconVisible = true,
                        expandableContent = {
                            DirectionExpandedContent(directionInfos = directionInfos)
                        }
                    )
                } else {
                    // 데이터가 없는 경우 기본 카드 표시
                    CommonStationCard(
                        primaryText = station,
                        secondText = "도착 정보 없음",
                        expandableIconVisible = false
                    )
                }
            }
        }
    }
}

@Composable
private fun DirectionExpandedContent(
    directionInfos: List<DirectionArrivalInfo>
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        directionInfos.forEach { directionInfo ->
            DirectionSection(directionInfo = directionInfo)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun DirectionSection(
    directionInfo: DirectionArrivalInfo
) {
    Column {
        // 방향 제목
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "▶ ${directionInfo.direction}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "(${directionInfo.upDownLine})",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 해당 방향의 모든 도착 정보
        directionInfo.arrivals.forEachIndexed { index, arrival ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "•",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = arrival.message,
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }
        }
    }
}
