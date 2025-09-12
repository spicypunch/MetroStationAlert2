package kr.jm.common_ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun CommonStationCard(
    primaryText: String,
    secondText: String,
    isBookmark: Boolean = false,
    bookMarkIconVisible: Boolean = false,
    expandableIconVisible: Boolean = false,
    onClickNotificationIcon: (String) -> Unit = {},
    onClickBookmarkIcon: (String) -> Unit = {}
) {
    var isExpandable by remember { mutableStateOf(false) }

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

            if (expandableIconVisible) {

                IconButton(onClick = { isExpandable = !isExpandable }) {
                    Icon(
                        imageVector = if (isExpandable) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Notifications",
                        tint = Color.Gray
                    )

                }
            }
        }
    }
}