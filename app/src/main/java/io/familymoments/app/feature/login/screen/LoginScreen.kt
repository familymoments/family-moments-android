package io.familymoments.app.feature.login.screen

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.AppBarScreen
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.core.util.FMVisualTransformation
import io.familymoments.app.core.util.noRippleClickable
import io.familymoments.app.core.util.oneClick
import io.familymoments.app.feature.forgotpassword.activity.ForgotPasswordActivity
import io.familymoments.app.feature.login.uistate.LoginUiState
import io.familymoments.app.feature.login.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    routeToSignUp: (LoginUiState) -> Unit = { _ -> },
    routeToMainActivity: () -> Unit = {}
) {
    val loginUiState = viewModel.loginUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val goToForgotPassword = {
        context.startActivity(Intent(context, ForgotPasswordActivity::class.java))
    }

    LaunchedEffect(loginUiState.value.isSuccess) {
        if (loginUiState.value.isSuccess == true) {
            if (loginUiState.value.isNeedToSignUp == true) {
                routeToSignUp(loginUiState.value)
            } else {
                routeToMainActivity()
            }
        }
    }

    AppBarScreen(title = {
        Text(
            text = stringResource(R.string.login_app_bar_screen_header),
            style = AppTypography.SH3_16,
            color = AppColors.deepPurple1
        )
    }) {
        LoginScreen(
            login = viewModel::loginUser,
            loginUiState.value,
            onRouteToSignUp = { routeToSignUp(loginUiState.value) },
            viewModel::updateSuccessNull,
            kakaoLogin = { viewModel.kakaoLogin(context) },
            naverLogin = { viewModel.naverLogin(context) },
            goToForgotPassword = goToForgotPassword
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    login: (String, String) -> Unit,
    loginUiState: LoginUiState,
    onRouteToSignUp: () -> Unit = {},
    updateSuccessNull: () -> Unit = {},
    kakaoLogin: () -> Unit = {},
    naverLogin: () -> Unit = {},
    goToForgotPassword: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxHeight()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginLogo()
        LoginForm(
            login = login,
            loginUiState = loginUiState,
            updateSuccessNull = updateSuccessNull
        )
        LoginOption(
           goToForgotPassword = goToForgotPassword,
           onRouteToSignUp = onRouteToSignUp
        )
        SocialLogin(kakaoLogin, naverLogin)
    }
}

@Composable
fun LoginLogo() {
    Row(modifier = Modifier.padding(top = 86.dp, bottom = 49.dp)) {
        Icon(
            modifier = Modifier.size(110.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_splash_icon),
            tint = Color.Unspecified,
            contentDescription = null,
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(
                text = stringResource(id = R.string.login_description_01),
                fontSize = 24.sp,
                color = AppColors.deepPurple1,
                style = AppTypography.H2_24,
            )
            Text(
                modifier = Modifier.padding(top = 13.dp),
                text = stringResource(id = R.string.login_description_02),
                fontSize = 13.sp,
                color = AppColors.grey2,
                style = AppTypography.LB1_13
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun LoginForm(
    login: (String, String) -> Unit,
    loginUiState: LoginUiState,
    updateSuccessNull: () -> Unit
) {
    var id by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current
    val requester = remember { BringIntoViewRequester() }

    Column(modifier = Modifier.padding(16.dp)) {
        LoginFormRoundedCornerTextField(
            label = stringResource(R.string.login_id_text_field_hint),
            onValueChanged = {
                id = it
                updateSuccessNull()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(
                    focusDirection = FocusDirection.Next,
                )
            }),
            requester = requester
        )
        Spacer(modifier = Modifier.height(8.dp))
        LoginFormRoundedCornerTextField(
            label = stringResource(R.string.login_password_text_field_hint),
            onValueChanged = {
                password = it
                updateSuccessNull()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                login(id.text, password.text)
                focusManager.clearFocus()
            }),
            requester = requester,
            showText = false
        )
        if (loginUiState.isSuccess == false) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = loginUiState.errorMessage ?: stringResource(R.string.login_default_error_message),
                style = AppTypography.BTN6_13,
                color = AppColors.red2,
            )
        } else {
            Box(modifier = Modifier.height(37.dp))
        }
        Surface(shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.bringIntoViewRequester(requester),
                onClick = {
                    login(id.text, password.text)
                    focusManager.clearFocus()
                },
                colors =
                ButtonDefaults.buttonColors(
                    containerColor = AppColors.deepPurple1,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White,
                ),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.login_login),
                    fontSize = 18.sp,
                    style = AppTypography.BTN4_18
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginFormRoundedCornerTextField(
    modifier: Modifier = Modifier,
    label: String,
    onValueChanged: (TextFieldValue) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    requester: BringIntoViewRequester,
    showText: Boolean = true,
) {
    var value by remember { mutableStateOf(TextFieldValue()) }
    val scope = rememberCoroutineScope()
    Surface(shape = RoundedCornerShape(8.dp), modifier = modifier.then(Modifier.fillMaxWidth())) {
        TextField(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    scope.launch {
                        delay(300)
                        requester.bringIntoView()
                    }
                }
            },
            value = value,
            onValueChange = {
                value = it
                onValueChanged(value)
            },
            visualTransformation = if (showText) VisualTransformation.None else FMVisualTransformation(),
            keyboardOptions = if (showText) keyboardOptions else keyboardOptions.copy(keyboardType = KeyboardType.Password),
            keyboardActions = keyboardActions,
            singleLine = true,
            label = { Text(label, color = AppColors.grey3, style = AppTypography.B1_16) },
            colors =
            TextFieldDefaults.colors(
                focusedContainerColor = AppColors.pink4,
                unfocusedContainerColor = AppColors.pink4,
                disabledContainerColor = AppColors.pink4,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
        )
    }
}

@Composable
fun LoginOption(
    goToForgotPassword: () -> Unit,
    onRouteToSignUp: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(top = 32.dp)
    ) {
        Text(
            text = stringResource(id = R.string.login_forgot_id),
            fontSize = 13.sp,
            color = AppColors.grey2,
            style = AppTypography.BTN6_13
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = AppColors.grey2
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.noRippleClickable { goToForgotPassword() },
            text = stringResource(id = R.string.login_forgot_pw),
            fontSize = 13.sp,
            color = AppColors.grey2,
            style = AppTypography.BTN6_13
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = AppColors.grey2
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.oneClick {
                onRouteToSignUp()
            },
            text = stringResource(id = R.string.login_signup),
            fontSize = 13.sp,
            color = AppColors.grey2,
            style = AppTypography.BTN6_13
        )
    }
}

@Composable
fun SocialLogin(
    kakaoLogin: () -> Unit,
    naverLogin: () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 23.dp, start = 17.dp, end = 17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
        )
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 4.dp),
            text = stringResource(R.string.sns_login),
            color = AppColors.grey2,
            fontSize = 13.sp,
            style = AppTypography.BTN6_13
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
        )
    }
    Spacer(modifier = Modifier.height(31.dp))
    Row(
        horizontalArrangement = Arrangement.spacedBy(37.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_kakao_login),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .oneClick(400, kakaoLogin)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_naver_login),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .oneClick(400, naverLogin)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    FamilyMomentsTheme {
        LoginScreen(
            login = { _, _ -> },
            loginUiState = LoginUiState(),
            updateSuccessNull = {},
            goToForgotPassword = {}
        )
    }
}
