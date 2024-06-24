package io.familymoments.app.feature.notification

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.familymoments.app.core.theme.FamilyMomentsTheme


@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val stateText = viewModel.stateText.collectAsState()

    NotificationScreenUI(
        stateText.value,
        onClick = {
            viewModel.uploadAlarm()
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotificationScreenUI(stateText: String = "", onClick: () -> Unit = {}){
    Scaffold {
        Column {
            Text(text = stateText, style = TextStyle(fontSize = 30.sp, color = Color.Blue))
            Button(onClick = onClick) {
                Text(text = "업로드 알림 테스트")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    FamilyMomentsTheme {
        NotificationScreenUI(stateText = "test")
    }
}
