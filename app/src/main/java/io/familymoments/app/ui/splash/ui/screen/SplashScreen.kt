package io.familymoments.app.ui.splash.ui.screen

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.R
import io.familymoments.app.model.uistate.SplashUiState
import io.familymoments.app.ui.login.ui.activity.LoginActivity
import io.familymoments.app.ui.bottomnav.ui.activity.MainActivity
import io.familymoments.app.ui.theme.FamilyMomentsTheme
import io.familymoments.app.ui.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    val splashUiState = viewModel.splashUiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        checkValidate({ viewModel.checkUserValidation() }, splashUiState, context)
    }
    SplashScreen()
}

suspend fun checkValidate(check: () -> Unit, uiState: State<SplashUiState>, context: Context) {
    check()
    delay(1000L)
    if (uiState.value.isSuccess == true) {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    } else if (uiState.value.isSuccess == false) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.padding(horizontal = 22.dp),
            painter = painterResource(id = R.drawable.splash_title),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(top = 9.dp),
            text = stringResource(R.string.splash_title),
            color = Color(0xFFA9A9A9),
        )
        Image(
            modifier = Modifier.padding(top = 38.dp),
            painter = painterResource(id = R.drawable.ic_splash_icon),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
fun SplashPreview() {
    FamilyMomentsTheme {
        SplashScreen()
    }
}
