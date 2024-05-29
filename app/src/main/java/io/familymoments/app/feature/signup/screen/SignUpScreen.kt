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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import io.familymoments.app.core.component.SignUpTextFieldArea
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.feature.signup.uistate.SignUpInfoUiState
import io.familymoments.app.feature.signup.uistate.SignUpTermUiState
import io.familymoments.app.feature.signup.uistate.SignUpUiState
import io.familymoments.app.feature.signup.uistate.SignUpValidatedUiState
import io.familymoments.app.feature.signup.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(viewModel: SignUpViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    SignUpScreenUI(
        uiState = uiState,
        onResetUserIdDuplicatedPass = viewModel::resetUserIdDuplicatedPass,
        onResetEmailDuplicatedPass = viewModel::resetEmailDuplicatedPass,
        onResetSignUpResultSuccess = viewModel::resetSignUpResultSuccess,
        onCheckIdFormat = viewModel::checkIdFormat,
        onCheckIdDuplication = viewModel::checkIdDuplication,
        onCheckPasswordFormat = viewModel::checkPasswordFormat,
        onCheckEmailFormat = viewModel::checkEmailFormat,
        onCheckEmailDuplication = viewModel::checkEmailDuplication,
        onCheckNicknameFormat = viewModel::checkNicknameFormat,
        onCheckBirthDayFormat = viewModel::checkBirthDayFormat,
        onExecuteSignUp = viewModel::executeSignUp
    )
}


@Composable
fun SignUpScreenUI(
    uiState: State<SignUpUiState> = remember { mutableStateOf(SignUpUiState()) },
    onResetUserIdDuplicatedPass: () -> Unit = {},
    onResetEmailDuplicatedPass: () -> Unit = {},
    onResetSignUpResultSuccess: () -> Unit = {},
    onCheckIdFormat: (String) -> Unit = {},
    onCheckIdDuplication: (String) -> Unit = {},
    onCheckPasswordFormat: (String) -> Unit = {},
    onCheckEmailFormat: (String) -> Unit = {},
    onCheckEmailDuplication: (String) -> Unit = {},
    onCheckNicknameFormat: (String) -> Unit = {},
    onCheckBirthDayFormat: (String) -> Unit = {},
    onExecuteSignUp: (SignUpInfoUiState) -> Unit = {}
) {
    val context = LocalContext.current

    var password: String by remember { mutableStateOf("") }
    val defaultProfileImageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_profile)

    var allEssentialTermsAgree by remember {
        mutableStateOf(false)
    }

    var passwordSameCheck by remember {
        mutableStateOf(false)
    }
    var signUpInfoUiState: SignUpInfoUiState by remember {
        mutableStateOf(
            SignUpInfoUiState()
        )
    }

    LaunchedEffect(uiState.value.signUpValidatedUiState.userIdDuplicatedUiState) {
        showUserIdDuplicationCheckResult(
            uiState.value.signUpValidatedUiState.userIdDuplicatedUiState.isSuccess,
            context
        )
        onResetUserIdDuplicatedPass()
    }
    LaunchedEffect(uiState.value.signUpValidatedUiState.emailDuplicatedUiState) {
        showEmailDuplicationCheckResult(uiState.value.signUpValidatedUiState.emailDuplicatedUiState.isSuccess, context)
        onResetEmailDuplicatedPass()
    }

    LaunchedEffect(uiState.value.signUpResultUiState.isSuccess) {
        if (uiState.value.signUpResultUiState.isSuccess == true) {
            Toast.makeText(context, context.getString(R.string.sign_up_success), Toast.LENGTH_SHORT).show()
            (context as Activity).finish()
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
                color = AppColors.deepPurple1
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
            Spacer(modifier = Modifier.height(43.dp))
            IdField(
                userIdFormatValidated = uiState.value.signUpValidatedUiState.userIdFormValidated,
                checkIdFormat = onCheckIdFormat,
                checkIdDuplication = onCheckIdDuplication,
                resetUserIdDuplicatedPass = onResetUserIdDuplicatedPass,
                userIdDuplicated = uiState.value.signUpValidatedUiState.userIdDuplicatedUiState.duplicatedPass.not()
            ) {
                signUpInfoUiState = signUpInfoUiState.copy(id = it)
            }
            FirstPasswordField(
                passwordFormatValidated = uiState.value.signUpValidatedUiState.passwordFormValidated,
                checkPasswordFormat = onCheckPasswordFormat
            ) {
                password = it
                signUpInfoUiState = signUpInfoUiState.copy(password = it)
            }
            SecondPasswordField(
                firstPassword = signUpInfoUiState.password,
                checkPasswordIsSame = { passwordSameCheck = it },
            )
            NameField { signUpInfoUiState = signUpInfoUiState.copy(name = it) }
            EmailField(
                checkEmailFormat = onCheckEmailFormat,
                checkEmailDuplication = onCheckEmailDuplication,
                emailFormatValidated = uiState.value.signUpValidatedUiState.emailFormValidated,
                resetEmailDuplicatedPass = onResetEmailDuplicatedPass,
                emailDuplicated = uiState.value.signUpValidatedUiState.emailDuplicatedUiState.duplicatedPass
            ) { signUpInfoUiState = signUpInfoUiState.copy(email = it) }
            BirthDayField(
                checkBirthDayFormat = onCheckBirthDayFormat,
                birthDayFormatValidated = uiState.value.signUpValidatedUiState.birthDayFormValidated
            ) {
                signUpInfoUiState = signUpInfoUiState.copy(birthDay = it)
            }
            NicknameField(
                nicknameFormatValidated = uiState.value.signUpValidatedUiState.nicknameFormValidated,
                checkNicknameFormat = onCheckNicknameFormat,
            ) { signUpInfoUiState = signUpInfoUiState.copy(nickname = it) }
            ProfileImageField(defaultProfileImageBitmap, context) {
                signUpInfoUiState = signUpInfoUiState.copy(imgFile = it)
            }
            Spacer(modifier = Modifier.height(53.dp))
            TermsField { allEssentialTermsAgree = it }
            StartButtonField(
                signUpInfoUiState,
                passwordSameCheck,
                allEssentialTermsAgree,
                uiState.value.signUpValidatedUiState,
            ) {
                onExecuteSignUp(signUpInfoUiState)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

private fun showEmailDuplicationCheckResult(emailDuplicated: Boolean?, context: Context) {
    if (emailDuplicated == true) {
        Toast.makeText(
            context,
            context.getString(R.string.sign_up_check_email_duplication_success),
            Toast.LENGTH_SHORT
        ).show()
    } else if (emailDuplicated == false) {
        Toast.makeText(context, context.getString(R.string.sign_up_check_email_duplication_fail), Toast.LENGTH_SHORT)
            .show()
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
    firstPassword: String,
    checkPasswordIsSame: (Boolean) -> Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    var isPasswordSame by remember {
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
                isPasswordSame = firstPassword == it.text
                checkPasswordIsSame(isPasswordSame)
            },
            showDeleteButton = true,
            showWarningText = true,
            warningText = stringResource(id = R.string.sign_up_password_check_validation_warning),
            validated = isPasswordSame,
            isFocused = isFocused,
            showText = false
        )
    }
}

@Composable
private fun EmailField(
    checkEmailFormat: (String) -> Unit,
    checkEmailDuplication: (String) -> Unit,
    emailFormatValidated: Boolean,
    resetEmailDuplicatedPass: () -> Unit,
    emailDuplicated: Boolean,
    onValueChange: (String) -> Unit
) {
    var previousEmail by remember {
        mutableStateOf(TextFieldValue())
    }
    var isFocused by remember {
        mutableStateOf(false)
    }
    Column {
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
                    resetEmailDuplicatedPass()
                    previousEmail = it
                }
            },
            showCheckButton = true,
            checkButtonAvailable = emailFormatValidated,
            onCheckButtonClick = {
                checkEmailDuplication(it.text)
            },
            isFocused = isFocused,
            showWarningText = true,
            warningText = if (emailFormatValidated) stringResource(id = R.string.sign_up_need_duplication_check_warning)
            else stringResource(id = R.string.sign_up_email_validation_warning),
            validated = if (emailFormatValidated) emailDuplicated else false
        )
    }
}

@Composable
private fun StartButtonField(
    signUpInfoUiState: SignUpInfoUiState,
    passwordSameCheck: Boolean,
    allEssentialTermsAgree: Boolean,
    signUpValidatedUiState: SignUpValidatedUiState,
    onClick: () -> Unit,
) {
    var signUpEnable by remember {
        mutableStateOf(false)
    }
    val signUpValidated = with(signUpValidatedUiState) {
        birthDayFormValidated
            && emailFormValidated
            && nicknameFormValidated
            && passwordFormValidated
            && userIdFormValidated
            && emailDuplicatedUiState.duplicatedPass
            && userIdDuplicatedUiState.duplicatedPass
    }
    signUpEnable = passwordSameCheck && allEssentialTermsAgree && signUpValidated && signUpInfoUiState.imgFile != null
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
    onCheckedChange: (CheckedStatus) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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

@Composable
fun TermsList(
    list: List<SignUpTermUiState>,
    onTermCheckedChange: (Int, CheckedStatus) -> Unit,
    onTermsCheckedChange: (Boolean) -> Unit
) {
    onTermsCheckedChange(list.filter { it.isEssential }.all { it.checkedStatus == CheckedStatus.CHECKED })
    for (index in list.indices) {
        TermItem(
            imageResources = listOf(R.drawable.uncheck, R.drawable.check),
            description = list[index].description,
            checked = list[index].checkedStatus,
            fontSize = 13,
            onCheckedChange = { onTermCheckedChange(index, it) })
    }
}

@Composable
fun TermsField(onAllEssentialTermsAgree: (Boolean) -> Unit) {

    val terms = remember {
        mutableStateListOf(
            SignUpTermUiState(true, R.string.sign_up_service_term_agree, CheckedStatus.UNCHECKED),
            SignUpTermUiState(true, R.string.sign_up_identification_term_agree, CheckedStatus.UNCHECKED),
            SignUpTermUiState(false, R.string.sign_up_marketing_alarm_term_agree, CheckedStatus.UNCHECKED)
        )
    }

    Column {
        TermItem(
            imageResources = listOf(R.drawable.circle_uncheck, R.drawable.circle_check),
            description = R.string.sign_up_all_term_agree,
            checked = if (terms.all { it.checkedStatus == CheckedStatus.CHECKED }) CheckedStatus.CHECKED else CheckedStatus.UNCHECKED,
            fontSize = 16,
            onCheckedChange = {
                for (index in terms.indices) {
                    terms[index] = terms[index].copy(checkedStatus = it)
                }
            })
        HorizontalDivider(modifier = Modifier.padding(vertical = 11.dp), thickness = 1.dp, color = AppColors.grey2)
        TermsList(list = terms, { index, checkedStatus ->
            terms[index] = terms[index].copy(checkedStatus = checkedStatus)
        }) { onAllEssentialTermsAgree(it) }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    FamilyMomentsTheme {
        SignUpScreenUI()
    }
}
