package kr.jm.common_ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommonStationCard(
    primaryText: String,
    secondText: String,
    isBookmark: Boolean = false,
    bookMarkIconVisible: Boolean = false,
    expandableIconVisible: Boolean = false,
    onClickNotificationIcon: (String) -> Unit = {},
    onClickBookmarkIcon: (String) -> Unit = {},
    expandableContent: (@Composable () -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = primaryText,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = secondText,
                        color = Color.Gray
                    )
                }

                if (bookMarkIconVisible) {
                    IconButton(onClick = { onClickNotificationIcon(primaryText) }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.Gray,
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(onClick = { onClickBookmarkIcon(primaryText) }) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = if (isBookmark) "Bookmarked" else "Not bookmarked",
                            tint = if (isBookmark) Color(0xFFFFD700) else Color.Gray,
                        )
                    }
                }

                // 확장/축소 아이콘
                if (expandableIconVisible) {
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = Color.Gray
                        )
                    }
                }
            }

            // 확장 가능한 콘텐츠 영역
            AnimatedVisibility(
                visible = isExpanded && expandableIconVisible,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    HorizontalDivider(
                        color = Color.Gray.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )
                    if (expandableContent != null) {
                        expandableContent()
                    } else {
                        // 기본 확장 콘텐츠
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "도착 정보",
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "여기에 추가 정보가 표시됩니다.",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}