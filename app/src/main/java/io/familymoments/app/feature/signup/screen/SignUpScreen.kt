package io.familymoments.app.feature.signup.screen

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.AppBarScreen
import io.familymoments.app.core.component.CheckedStatus
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.component.FMCheckBox
import io.familymoments.app.core.component.LoadingIndicator
import io.familymoments.app.core.component.SignUpTextFieldArea
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.core.util.FileUtil
import io.familymoments.app.feature.signup.uistate.SignUpInfoUiState
import io.familymoments.app.feature.signup.uistate.SignUpUiState
import io.familymoments.app.feature.signup.uistate.SignUpValidatedUiState
import io.familymoments.app.feature.signup.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(viewModel: SignUpViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    SignUpScreenUI(
        uiState = uiState,
        resetUserIdDuplicatedPass = viewModel::resetUserIdDuplicatedPass,
        resetEmailVerified = viewModel::resetEmailVerified,
        resetSignUpResultSuccess = viewModel::resetSignUpResultSuccess,
        resetPostSuccess = viewModel::resetPostSuccess,
        checkIdFormat = viewModel::checkIdFormat,
        checkIdDuplication = viewModel::checkIdDuplication,
        checkPasswordFormat = viewModel::checkPasswordFormat,
        checkEmailFormat = viewModel::checkEmailFormat,
        sendEmailVerificationCode = viewModel::sendEmailVerificationCode,
        checkNicknameFormat = viewModel::checkNicknameFormat,
        executeSignUp = viewModel::executeSignUp,
        verifyEmailVerificationCode = viewModel::verifyEmailVerificationCode,
        updateSignUpInfoUiState = viewModel::updateSignUpInfo,
        checkPasswordSame = viewModel::checkPasswordSame,
        resetVerifyCodeAvailable = viewModel::resetVerifyCodeAvailable
    )
}


@Composable
fun SignUpScreenUI(
    uiState: State<SignUpUiState> = remember { mutableStateOf(SignUpUiState()) },
    resetUserIdDuplicatedPass: () -> Unit = {},
    resetEmailVerified: () -> Unit = {},
    resetSignUpResultSuccess: () -> Unit = {},
    resetPostSuccess: () -> Unit = {},
    checkIdFormat: (String) -> Unit = {},
    checkIdDuplication: (String) -> Unit = {},
    checkPasswordFormat: (String) -> Unit = {},
    checkEmailFormat: (String) -> Unit = {},
    sendEmailVerificationCode: (String) -> Unit = {},
    checkNicknameFormat: (String) -> Unit = {},
    executeSignUp: (SignUpInfoUiState) -> Unit = {},
    verifyEmailVerificationCode: (String) -> Unit = {},
    updateSignUpInfoUiState: (SignUpInfoUiState) -> Unit = {},
    checkPasswordSame: (String) -> Unit = {},
    resetVerifyCodeAvailable: () -> Unit = {}
) {
    val context = LocalContext.current
    val isDefaultProfileImage = remember { mutableStateOf(false) }
    val defaultProfileImageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_profile)
    var allEssentialTermsAgree by remember {
        mutableStateOf(false)
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

        LaunchedEffectWithUiState(
            uiState = uiState.value,
            context = context,
            resetSignUpResultSuccess = resetSignUpResultSuccess,
            resetPostSuccess = resetPostSuccess,
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(43.dp))
            IdField(
                userIdFormatValidated = uiState.value.signUpValidatedUiState.userIdFormValidated,
                checkIdFormat = checkIdFormat,
                checkIdDuplication = checkIdDuplication,
                resetUserIdDuplicatedPass = resetUserIdDuplicatedPass,
                userIdDuplicated = uiState.value.signUpValidatedUiState.userIdDuplicatedPass
            ) {
                updateSignUpInfoUiState(uiState.value.signUpInfoUiState.copy(id = it))
            }
            FirstPasswordField(
                passwordFormatValidated = uiState.value.signUpValidatedUiState.passwordFormValidated,
                checkPasswordFormat = checkPasswordFormat
            ) {
                updateSignUpInfoUiState(uiState.value.signUpInfoUiState.copy(password = it))
            }
            SecondPasswordField(
                checkPasswordSame = checkPasswordSame,
                passwordSameCheck = uiState.value.signUpValidatedUiState.passwordSameCheck
            )
            EmailField(
                checkEmailFormat = checkEmailFormat,
                sendEmailVerificationCode = sendEmailVerificationCode,
                emailFormatValidated = uiState.value.signUpValidatedUiState.emailFormValidated,
                resetEmailVerified = resetEmailVerified,
                expirationTime = uiState.value.expirationTimeUiState.expirationTime,
                isExpirationTimeVisible = uiState.value.expirationTimeUiState.isExpirationTimeVisible,
                sendEmailVerificationCodeAvailable = uiState.value.verificationCodeButtonUiState.sendEmailAvailable,
                verifyEmailVerificationCode = verifyEmailVerificationCode,
                resetVerifyCodeAvailable = resetVerifyCodeAvailable,
                verifyCodeAvailable = uiState.value.verificationCodeButtonUiState.verifyCodeAvailable
            ) {
                updateSignUpInfoUiState(uiState.value.signUpInfoUiState.copy(email = it))
            }
            NicknameField(
                nicknameFormatValidated = uiState.value.signUpValidatedUiState.nicknameFormValidated,
                checkNicknameFormat = checkNicknameFormat,
            ) { updateSignUpInfoUiState(uiState.value.signUpInfoUiState.copy(nickname = it)) }
            ProfileImageField(defaultProfileImageBitmap, context, isDefaultProfileImage) {
                updateSignUpInfoUiState(uiState.value.signUpInfoUiState.copy(imgFile = it))
            }
            Spacer(modifier = Modifier.height(53.dp))
            TermsField(onAllEssentialTermsAgree = { allEssentialTermsAgree = it })
            StartButtonField(
                uiState.value.signUpInfoUiState,
                allEssentialTermsAgree,
                uiState.value.signUpValidatedUiState,
                isDefaultProfileImage.value,
            ) {
                var signUpInfoUiState = uiState.value.signUpInfoUiState
                if (isDefaultProfileImage.value) {
                    signUpInfoUiState = signUpInfoUiState.copy(
                        imgFile = FileUtil.getDefaultProfileImage(context)
                    )
                }

                executeSignUp(signUpInfoUiState)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun LaunchedEffectWithUiState(
    uiState: SignUpUiState,
    context: Context,
    resetSignUpResultSuccess: () -> Unit,
    resetPostSuccess: () -> Unit
) {

    var isLoading: Boolean by remember { mutableStateOf(false) }

    LoadingIndicator(isLoading = isLoading)

    LaunchedEffect(uiState.isLoading) {
        isLoading = uiState.isLoading
    }

    LaunchedEffect(uiState.postSuccess) {
        uiState.postSuccess?.let {
            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            resetPostSuccess()
        }
    }

    LaunchedEffect(uiState.signUpSuccess) {
        if (uiState.signUpSuccess == true) {
            Toast.makeText(context, context.getString(R.string.sign_up_success), Toast.LENGTH_SHORT).show()
            (context as Activity).finish()
        } else if (uiState.signUpSuccess == false) {
            val errorMessage = uiState.message.ifEmpty {
                context.getString(R.string.sign_up_fail)
            }
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        resetSignUpResultSuccess()
    }
}


@Composable
fun FirstPasswordField(
    passwordFormatValidated: Boolean,
    checkPasswordFormat: (String) -> Unit,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    SignUpTextFieldArea(
        modifier = Modifier.onFocusChanged {
            isFocused = it.isFocused
        },
        title = stringResource(id = R.string.sign_up_password_field_title),
        hint = stringResource(R.string.sign_up_password_field_hint),
        onValueChange = {
            onValueChange(it.text)
            checkPasswordFormat(it.text)
        },
        showDeleteButton = true,
        showWarningText = true,
        warningText = stringResource(id = R.string.sign_up_password_validation_warning),
        validated = passwordFormatValidated,
        isFocused = isFocused,
        showText = false
    )
}

@Composable
fun SecondPasswordField(
    checkPasswordSame: (String) -> Unit,
    passwordSameCheck: Boolean
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    Column {
        SignUpTextFieldArea(
            modifier = Modifier.onFocusChanged {
                isFocused = it.isFocused
            },
            title = stringResource(id = R.string.sign_up_password_check_field_title),
            hint = stringResource(R.string.sign_up_password_check_field_hint),
            onValueChange = {
                checkPasswordSame(it.text)
            },
            showDeleteButton = true,
            showWarningText = true,
            warningText = stringResource(id = R.string.sign_up_password_check_validation_warning),
            validated = passwordSameCheck,
            isFocused = isFocused,
            showText = false
        )
    }
}


@Composable
private fun EmailField(
    checkEmailFormat: (String) -> Unit,
    sendEmailVerificationCode: (String) -> Unit,
    emailFormatValidated: Boolean,
    resetEmailVerified: () -> Unit,
    expirationTime: Int,
    isExpirationTimeVisible: Boolean,
    sendEmailVerificationCodeAvailable: Boolean,
    verifyEmailVerificationCode: (String) -> Unit,
    resetVerifyCodeAvailable: () -> Unit,
    verifyCodeAvailable: Boolean,
    onValueChange: (String) -> Unit,
) {
    Column {
        EmailTextField(
            checkEmailFormat = checkEmailFormat,
            sendEmailVerificationCode = sendEmailVerificationCode,
            emailFormatValidated = emailFormatValidated,
            resetEmailVerified = resetEmailVerified,
            sendEmailVerificationCodeAvailable = sendEmailVerificationCodeAvailable,
            onValueChange = onValueChange
        )
        VerificationCodeTextField(
            verifyEmailVerificationCode = verifyEmailVerificationCode,
            expirationTime = expirationTime,
            isExpirationTimeVisible = isExpirationTimeVisible,
            resetVerifyCodeAvailable = resetVerifyCodeAvailable,
            verifyCodeAvailable = verifyCodeAvailable
        )

    }
}

@Composable
private fun EmailTextField(
    checkEmailFormat: (String) -> Unit,
    sendEmailVerificationCode: (String) -> Unit,
    emailFormatValidated: Boolean,
    resetEmailVerified: () -> Unit,
    sendEmailVerificationCodeAvailable: Boolean,
    onValueChange: (String) -> Unit
) {
    var previousEmail by remember {
        mutableStateOf(TextFieldValue())
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    SignUpTextFieldArea(
        modifier = Modifier.onFocusChanged {
            isFocused = it.isFocused
        },
        title = stringResource(R.string.sign_up_email_field_title),
        hint = stringResource(id = R.string.sign_up_email_field_hint),
        onValueChange = {
            if (it.text != previousEmail.text) {
                onValueChange(it.text)
                checkEmailFormat(it.text)
                resetEmailVerified()
                previousEmail = it
            }
        },
        showCheckButton = true,
        checkButtonAvailable = emailFormatValidated && sendEmailVerificationCodeAvailable,
        onCheckButtonClick = { emailTextField ->
            sendEmailVerificationCode(emailTextField.text)
        },
        isFocused = isFocused,
        showWarningText = true,
        warningText = if (!emailFormatValidated) stringResource(id = R.string.sign_up_email_validation_warning) else "",
        validated = emailFormatValidated,
        checkButtonLabel = stringResource(R.string.sign_up_send_verification_code_label),
        warningTextAreaHeight = 12
    )
}

@Composable
private fun VerificationCodeTextField(
    verifyEmailVerificationCode: (String) -> Unit,
    expirationTime: Int,
    isExpirationTimeVisible: Boolean,
    resetVerifyCodeAvailable: () -> Unit = {},
    verifyCodeAvailable: Boolean
) {
    var verificationCode by remember {
        mutableStateOf("")
    }
    var isFocused by remember {
        mutableStateOf(false)
    }
    SignUpTextFieldArea(
        modifier = Modifier.onFocusChanged {
            isFocused = it.isFocused
        },
        hint = stringResource(R.string.sign_up_verification_code_hint),
        onValueChange = {
            verificationCode = it.text
            resetVerifyCodeAvailable()
        },
        showCheckButton = true,
        checkButtonAvailable = verificationCode.isNotEmpty() && verifyCodeAvailable,
        onCheckButtonClick = {
            verifyEmailVerificationCode(verificationCode)
        },
        isFocused = isFocused,
        showWarningText = false,
        checkButtonLabel = stringResource(R.string.sign_up_verify_verification_code_label),
        showDeleteButton = true,
    )
    if (isExpirationTimeVisible) {
        Text(
            modifier = Modifier.padding(top = 2.dp, bottom = 20.dp),
            text = stringResource(R.string.sign_up_expiration_tiem_label, formatExpirationTime(expirationTime)),
            color = AppColors.red2,
            style = AppTypography.BTN6_13
        )
    } else {
        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun formatExpirationTime(expirationTime: Int): String {
    val minutes = expirationTime / 60
    val seconds = expirationTime % 60
    return "${minutes}:${if (seconds < 10) "0$seconds" else seconds}"
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
        nicknameFormValidated
            && passwordFormValidated
            && passwordSameCheck
            && userIdDuplicatedPass
            && emailVerified
    }
    signUpEnable =
        allEssentialTermsAgree && signUpValidated && (signUpInfoUiState.imgFile != null || isDefaultProfileImage)
    FMButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = signUpEnable,
        text = stringResource(R.string.sign_up_btn)
    )
}

@Composable
fun TermItem(
    imageResources: List<Int>,
    description: Int,
    checked: CheckedStatus,
    fontSize: Int,
    onCheckedChange: (CheckedStatus) -> Unit = {},
    onShowDetail: () -> Unit = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable(onClick = onShowDetail)) {
        FMCheckBox(
            imageResources = imageResources,
            defaultStatus = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            modifier = Modifier.padding(start = 14.dp),
            text = stringResource(id = description),
            fontSize = fontSize.sp
        )
    }
}



@Preview(showBackground = true, heightDp = 1500)
@Composable
fun SignUpScreenPreview() {
    FamilyMomentsTheme {
        SignUpScreenUI()
    }
}
