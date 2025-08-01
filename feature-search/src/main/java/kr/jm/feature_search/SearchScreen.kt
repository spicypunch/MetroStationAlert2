package kr.jm.feature_search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kr.jm.common_ui.theme.bgColor

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchScreenState by searchViewModel.searchScreenState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("하차 알리미🔔")
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchScreenState.searchQuery,
                onValueChange = searchViewModel::onSearchQueryChanged,
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(12.dp))
                    .height(55.dp)
                    .weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Transparent,
                    unfocusedTextColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {},
                modifier = Modifier
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(12.dp))
                    .background(Color.Black, shape = RoundedCornerShape(12.dp))
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("검색")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, shape = RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(
                 containerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("등록된 역", fontWeight = FontWeight.Bold)
                Text("선릉역")
            }
        }

        DropdownMenu(
            expanded = searchScreenState.dropDownExpanded,
            onDismissRequest = {}
        ) {
            DropdownMenuItem(
                text = { Text("노선 선택")},
                onClick = {}
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(searchScreenState.stations.size) { index ->
                val station = searchScreenState.stations[index]
                StationItem(station = station)
            }
        }
    }
}

@Composable
private fun StationItem(
    station: kr.jm.domain.model.SubwayStation,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
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
                    text = station.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = station.line,
                    color = Color.Gray
                )
            }
            
            Icon(
                imageVector = if (station.isBookmarked) {
                    Icons.Filled.Star
                } else {
                    Icons.Outlined.Star
                },
                contentDescription = if (station.isBookmarked) "Bookmarked" else "Not bookmarked",
                tint = if (station.isBookmarked) Color(0xFFFFD700) else Color.Gray
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen()
}