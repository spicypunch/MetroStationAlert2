package kr.jm.feature_settings

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@SuppressLint("DefaultLocale")
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsScreenState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Show snackbar for errors
    LaunchedEffect(settingsScreenState.errorMessage) {
        settingsScreenState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            settingsViewModel.clearErrorMessage()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "설정⚙\uFE0F",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column() {
            SetNotiTitle(
                settingsScreenState,
                onValueChange = { settingsViewModel.onNotificationTitleChanged(it) },
                onClickButton = {
                    settingsViewModel.saveNotificationTitle()
                    keyboardController?.hide()
                }
            )


            Spacer(modifier = Modifier.height(24.dp))

            SetNotiContent(
                settingsScreenState,
                onValueChange = { settingsViewModel.onNotificationContentChanged(it) },
                onClickButton = {
                    settingsViewModel.saveNotificationContent()
                    keyboardController?.hide()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SetSlider(
                settingsScreenState,
                onShowBottomSheet = { settingsViewModel.onShowBottomSheet() },
                onHideBottomSheet = { settingsViewModel.onHideBottomSheet() },
                onAlertDistanceChanged = { settingsViewModel.onAlertDistanceChanged(it) },
                saveAlertDistance = { settingsViewModel.saveAlertDistance() }
            )
        }

        // Snackbar Host
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
fun SetNotiTitle(
    settingsScreenState: SettingsScreenState,
    onValueChange: (String) -> Unit,
    onClickButton: () -> Unit,
) {
    Text(
        text = "알림 제목을 설정해 주세요.",
        modifier = Modifier.padding(bottom = 8.dp),
        color = Color.Black
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = settingsScreenState.notificationTitle,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .weight(1f)
                .height(55.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                onClickButton()
            },
            modifier = Modifier
                .height(55.dp)
                .background(Color.Black, RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("저장", color = Color.White)
        }
    }
}

@Composable
fun SetNotiContent(
    settingsScreenState: SettingsScreenState,
    onValueChange: (String) -> Unit,
    onClickButton: () -> Unit,
) {
    Text(
        text = "알림 내용을 설정해 주세요.",
        modifier = Modifier.padding(bottom = 8.dp),
        color = Color.Black
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = settingsScreenState.notificationContent,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .weight(1f)
                .height(55.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                onClickButton()
            },
            modifier = Modifier
                .height(55.dp)
                .background(Color.Black, RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("저장", color = Color.White)
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetSlider(
    settingsScreenState: SettingsScreenState,
    onShowBottomSheet: () -> Unit,
    onHideBottomSheet: () -> Unit,
    onAlertDistanceChanged: (Float) -> Unit,
    saveAlertDistance: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    Text(
        text = "역 도착 몇 km 전에 알림을 받으실 건가요?",
        modifier = Modifier.padding(bottom = 8.dp),
        color = Color.Black
    )
    Button(
        onClick = { onShowBottomSheet() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Text(
            text = String.format("%.1f", settingsScreenState.alertDistance) + "km",
            fontSize = 18.sp,
            color = Color.Black
        )
    }

    // Bottom Sheet for Distance Selection
    if (settingsScreenState.showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onHideBottomSheet() },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = String.format("%.1f", settingsScreenState.alertDistance) + "km",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Slider(
                    value = settingsScreenState.alertDistance,
                    onValueChange = { onAlertDistanceChanged(it) },
                    steps = 8,
                    valueRange = 0.5f..5.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color.Black,
                        inactiveTrackColor = Color.Gray,
                        inactiveTickColor = Color.White
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { saveAlertDistance() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("확인", color = Color.White)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen()
}