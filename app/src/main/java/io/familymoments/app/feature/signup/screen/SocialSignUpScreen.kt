package io.familymoments.app.feature.signup.screen

import android.app.Activity
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.AppBarScreen
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.component.SignUpTextFieldArea
import io.familymoments.app.core.network.dto.response.LoginResult
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.FileUtil
import io.familymoments.app.feature.signup.uistate.SignUpInfoUiState
import io.familymoments.app.feature.signup.uistate.SignUpUiState
import io.familymoments.app.feature.signup.uistate.SignUpValidatedUiState
import io.familymoments.app.feature.signup.viewmodel.SignUpViewModel

@Composable
fun SocialSignUpScreen(
    viewModel: SignUpViewModel,
    socialType: String,
    socialToken: String,
    loginResult: LoginResult,
    onRouteToMain: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    SocialSignUpScreenUI(
        uiState = uiState,
        loginResult = loginResult,
        onResetUserIdDuplicatedPass = viewModel::resetUserIdDuplicatedPass,
        onResetSignUpResultSuccess = viewModel::resetSignUpResultSuccess,
        onCheckIdFormat = viewModel::checkIdFormat,
        onCheckIdDuplication = viewModel::checkIdDuplication,
        onCheckNicknameFormat = viewModel::checkNicknameFormat,
        onCheckBirthDayFormat = viewModel::checkBirthDayFormat,
        onExecuteSignUp = {
            viewModel.executeSignUp(it, socialType, socialToken)
        },
        onRouteToMain = onRouteToMain
    )
}

@Composable
fun SocialSignUpScreenUI(
    uiState: State<SignUpUiState> = remember { mutableStateOf(SignUpUiState()) },
    loginResult: LoginResult = LoginResult(),
    onResetUserIdDuplicatedPass: () -> Unit = {},
    onResetSignUpResultSuccess: () -> Unit = {},
    onCheckIdFormat: (String) -> Unit = {},
    onCheckIdDuplication: (String) -> Unit = {},
    onCheckNicknameFormat: (String) -> Unit = {},
    onCheckBirthDayFormat: (String) -> Unit = {},
    onExecuteSignUp: (SignUpInfoUiState) -> Unit = {},
    onRouteToMain: () -> Unit = {},
) {


    val context = LocalContext.current
    val defaultProfileImageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_profile)

    var allEssentialTermsAgree by remember {
        mutableStateOf(false)
    }

    var signUpInfoUiState: SignUpInfoUiState by remember {
        mutableStateOf(
            SignUpInfoUiState(email = loginResult.email, name = loginResult.name, nickname = loginResult.nickname, birthDay = loginResult.strBirthDate)
        )
    }
    LaunchedEffect(uiState.value.signUpValidatedUiState.userIdDuplicatedUiState) {
        showUserIdDuplicationCheckResult(
            uiState.value.signUpValidatedUiState.userIdDuplicatedUiState.isSuccess,
            context
        )
    }

    LaunchedEffect(uiState.value.signUpResultUiState.isSuccess) {
        if (uiState.value.signUpResultUiState.isSuccess == true) {
            onRouteToMain()
        } else if (uiState.value.signUpResultUiState.isSuccess == false) {
            val errorMessage = uiState.value.signUpResultUiState.message.ifEmpty {
                context.getString(R.string.sign_up_fail)
            }
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        onResetSignUpResultSuccess()
    }

    AppBarScreen(
        title = {
            Text(
                text = stringResource(id = R.string.sign_up_activity_app_bar_title),
                style = AppTypography.SH3_16,
                color = AppColors.grey8
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier.clickable {
                    (context as Activity).finish()
                },
                painter = painterResource(id = R.drawable.ic_app_bar_back),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            val isDefaultProfileImage = remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(43.dp))
            IdField(
                userIdFormatValidated = uiState.value.signUpValidatedUiState.userIdFormValidated,
                checkIdFormat = onCheckIdFormat,
                checkIdDuplication = onCheckIdDuplication,
                resetUserIdDuplicatedPass = onResetUserIdDuplicatedPass,
                userIdDuplicated = uiState.value.signUpValidatedUiState.userIdDuplicatedUiState.duplicatedPass
            ) {
                signUpInfoUiState = signUpInfoUiState.copy(id = it)
            }
            NameField(default = loginResult.name) { signUpInfoUiState = signUpInfoUiState.copy(name = it) }
            EmailField(email = loginResult.email)

            BirthDayField(
                default = loginResult.strBirthDate,
                checkBirthDayFormat = onCheckBirthDayFormat,
                birthDayFormatValidated = uiState.value.signUpValidatedUiState.birthDayFormValidated
            ) {
                signUpInfoUiState = signUpInfoUiState.copy(birthDay = it)
            }
            NicknameField(
                default = loginResult.nickname,
                nicknameFormatValidated = uiState.value.signUpValidatedUiState.nicknameFormValidated,
                checkNicknameFormat = onCheckNicknameFormat,
            ) { signUpInfoUiState = signUpInfoUiState.copy(nickname = it) }
            ProfileImageField(defaultProfileImageBitmap, context, isDefaultProfileImage) {
                signUpInfoUiState = signUpInfoUiState.copy(imgFile = it)
            }
            Spacer(modifier = Modifier.height(53.dp))
            TermsField { allEssentialTermsAgree = it }
            StartButtonField(
                signUpInfoUiState,
                allEssentialTermsAgree,
                uiState.value.signUpValidatedUiState,
                isDefaultProfileImage.value
            ) {
                if (isDefaultProfileImage.value) {
                    signUpInfoUiState = signUpInfoUiState.copy(imgFile = FileUtil.getDefaultProfileImage(context))
                }
                onExecuteSignUp(signUpInfoUiState)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun EmailField(email: String) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    Column {
        SignUpTextFieldArea(
            modifier = Modifier.onFocusChanged {
                isFocused = it.isFocused
            },
            title = stringResource(R.string.email),
            hint = email,
            showCheckButton = false,
            isFocused = isFocused,
            showWarningText = true,
            enabled = false
        )
    }
}

@Composable
private fun StartButtonField(
    signUpInfoUiState: SignUpInfoUiState,
    allEssentialTermsAgree: Boolean,
    signUpValidatedUiState: SignUpValidatedUiState,
    isDefaultProfileImage: Boolean = false,
    onClick: () -> Unit,
) {
    var signUpEnable by remember {
        mutableStateOf(false)
    }
    val signUpValidated = with(signUpValidatedUiState) {
        birthDayFormValidated
            && nicknameFormValidated
            && userIdFormValidated
            && userIdDuplicatedUiState.duplicatedPass
    }
    signUpEnable = allEssentialTermsAgree && signUpValidated && (signUpInfoUiState.imgFile != null || isDefaultProfileImage)
    FMButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onClick()
        },
        enabled = signUpEnable,
        text = stringResource(R.string.sign_up_btn)
    )
}
