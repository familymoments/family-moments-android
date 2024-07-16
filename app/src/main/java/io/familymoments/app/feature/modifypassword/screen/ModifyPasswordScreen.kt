package io.familymoments.app.feature.modifypassword.screen

import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.component.FMTextField
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.login.activity.LoginActivity
import io.familymoments.app.feature.modifypassword.uistate.CurrentPasswordUiState
import io.familymoments.app.feature.modifypassword.uistate.NewPasswordCheckUiState
import io.familymoments.app.feature.modifypassword.uistate.NewPasswordUiState
import io.familymoments.app.feature.modifypassword.viewmodel.ModifyPasswordViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModifyPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ModifyPasswordViewModel
) {
    val currentPassword = remember { mutableStateOf(TextFieldValue()) }
    val newPassword = remember { mutableStateOf(TextFieldValue()) }
    val newPasswordCheck = remember { mutableStateOf(TextFieldValue()) }
    val requester = remember { BringIntoViewRequester() }
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val currentPasswordUiState = uiState.value.currentPasswordUiState
    val newPasswordUiState = uiState.value.newPasswordUiState
    val newPasswordCheckUiState = uiState.value.newPasswordCheckUiState
    val isNewPasswordValidated =
        uiState.value.newPasswordUiState.isValidated && uiState.value.newPasswordCheckUiState.isValidated

    LaunchedEffect(uiState.value.isSuccess) {
        if (uiState.value.isSuccess) {
            Toast.makeText(context, context.getString(R.string.modify_password_request_success), Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
    LaunchedEffect(currentPasswordUiState.isReset) {
        if (currentPasswordUiState.isReset) {
            currentPassword.value = TextFieldValue()
            viewModel.onClearCurrentPassword()
        }
    }
    LaunchedEffect(newPasswordUiState.isReset, newPasswordCheckUiState.isReset) {
        if (newPasswordUiState.isReset && newPasswordCheckUiState.isReset) {
            newPassword.value = TextFieldValue()
            newPasswordCheck.value = TextFieldValue()
            viewModel.onClearNewPasswords()
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ModifyPasswordTitle()
        Spacer(modifier = Modifier.height(40.dp))
        ModifyPasswordInfo()
        Spacer(modifier = Modifier.height(26.dp))
        CurrentPasswordField(
            currentPassword = currentPassword,
            currentPasswordUiState = currentPasswordUiState,
            onValueChange = {
                currentPassword.value = it
                viewModel.checkCurrentPassword(it.text)
            }
        )
        NewPasswordField(
            newPassword = newPassword,
            newPasswordCheck = newPasswordCheck,
            newPasswordUiState = newPasswordUiState,
            newPasswordCheckUiState = newPasswordCheckUiState,
            newPasswordOnValueChanged = {
                newPassword.value = it
                viewModel.checkPasswordFormat(it.text)
                viewModel.checkPasswordEqual(it.text, newPasswordCheck.value.text)
            },
            newPasswordCheckOnValueChanged = {
                newPasswordCheck.value = it
                viewModel.checkPasswordFormat(newPassword.value.text)
                viewModel.checkPasswordEqual(newPassword.value.text, it.text)
            },
            requester = requester,
        )
        ModifyPasswordButton(
            currentPasswordValid = currentPasswordUiState.isValidated,
            newPasswordValid = isNewPasswordValidated,
            requester = requester,
            onClick = {
                viewModel.requestModifyPassword(
                    currentPassword.value.text,
                    newPassword.value.text,
                    newPasswordCheck.value.text
                )
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ModifyPasswordTitle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    ) {
        Text(
            text = stringResource(id = R.string.modify_password_title),
            style = AppTypography.B1_16,
            color = AppColors.black1,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ModifyPasswordInfo() {
    Text(
        text = stringResource(id = R.string.modify_password_notification_1),
        style = AppTypography.B1_16,
        color = AppColors.grey8,
        modifier = Modifier.padding(bottom = 20.dp)
    )
    Text(
        text = stringResource(id = R.string.modify_password_notification_2),
        style = AppTypography.B1_16,
        color = AppColors.grey8
    )
}

@Composable
private fun CurrentPasswordField(
    currentPassword: State<TextFieldValue>,
    currentPasswordUiState: CurrentPasswordUiState,
    onValueChange: (TextFieldValue) -> Unit
) {
    ModifyPasswordTextField(
        onValueChange = onValueChange,
        value = currentPassword.value,
        hintResId = R.string.modify_password_current_password,
        showWarning = currentPasswordUiState.warningResId != null
    )
    ModifyPasswordWarning(
        warningResId = currentPasswordUiState.warningResId,
        bottomPadding = 70.dp,
        showWarning = currentPasswordUiState.warningResId != null
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NewPasswordField(
    newPassword: State<TextFieldValue>,
    newPasswordCheck: State<TextFieldValue>,
    newPasswordUiState: NewPasswordUiState,
    newPasswordCheckUiState: NewPasswordCheckUiState,
    newPasswordOnValueChanged: (TextFieldValue) -> Unit,
    newPasswordCheckOnValueChanged: (TextFieldValue) -> Unit,
    requester: BringIntoViewRequester,
) {
    val scope = rememberCoroutineScope()
    val onFocusChange: (Boolean) -> Unit = { isFocused ->
        scope.launch {
            if (isFocused) {
                delay(300)
                requester.bringIntoView()
            }
        }
    }
    ModifyPasswordTextField(
        onValueChange = newPasswordOnValueChanged,
        value = newPassword.value,
        hintResId = R.string.modify_password_new_password,
        showWarning = newPasswordUiState.showWarningBorder,
        onFocusChange = onFocusChange,
    )
    Spacer(modifier = Modifier.padding(top = 18.dp))
    ModifyPasswordTextField(
        onValueChange = newPasswordCheckOnValueChanged,
        value = newPasswordCheck.value,
        hintResId = R.string.modify_password_new_password_check,
        showWarning = newPasswordCheckUiState.showWarningBorder,
        onFocusChange = onFocusChange
    )
    ModifyPasswordWarning(
        warningResId = newPasswordUiState.warningResId ?: newPasswordCheckUiState.warningResId,
        bottomPadding = 67.dp,
        showWarning = newPasswordUiState.warningResId != null || newPasswordCheckUiState.warningResId != null
    )
}

@Composable
fun ModifyPasswordWarning(
    @StringRes warningResId: Int?,
    bottomPadding: Dp = 0.dp,
    showWarning: Boolean = false,
) {
    if (showWarning) {
        Box(
            modifier = Modifier
                .padding(top = 9.dp, bottom = bottomPadding - 25.dp)
        ) {
            if (warningResId != null) {
                Text(
                    text = stringResource(id = warningResId),
                    style = AppTypography.LB1_13,
                    color = AppColors.red2,
                    modifier = Modifier.height(16.dp)
                )
            }
        }
    } else {
        Spacer(modifier = Modifier.padding(top = bottomPadding))
    }
}

@Composable
fun ModifyPasswordTextField(
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit,
    value: TextFieldValue,
    @StringRes hintResId: Int,
    showWarning: Boolean,
    onFocusChange: (Boolean) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val keyboardActions = KeyboardActions(
        onDone = { focusManager.clearFocus() }
    )
    FMTextField(
        modifier = modifier
            .background(AppColors.pink5)
            .height(46.dp),
        onValueChange = onValueChange,
        value = value,
        hint = stringResource(id = hintResId),
        borderColor = if (showWarning) AppColors.red2 else AppColors.grey2,
        showDeleteButton = false,
        showText = false,
        onFocusChanged = onFocusChange,
        keyboardActions = keyboardActions
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModifyPasswordButton(
    currentPasswordValid: Boolean,
    newPasswordValid: Boolean,
    requester: BringIntoViewRequester,
    onClick: () -> Unit,
) {
    FMButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(59.dp)
            .bringIntoViewRequester(requester),
        onClick = onClick,
        text = stringResource(id = R.string.modify_password_btn),
        enabled = currentPasswordValid && newPasswordValid
    )
}

@Preview(showBackground = true)
@Composable
fun ModifyPasswordScreenPreview() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ModifyPasswordTitle()
        Spacer(modifier = Modifier.height(40.dp))
        ModifyPasswordInfo()
        Spacer(modifier = Modifier.height(26.dp))
        ModifyPasswordTextField(
            onValueChange = {},
            value = TextFieldValue("test1234"),
            hintResId = R.string.modify_password_current_password,
            showWarning = false
        )
        ModifyPasswordWarning(
            warningResId = null,
            bottomPadding = 70.dp,
            showWarning = false
        )
        ModifyPasswordTextField(
            onValueChange = {},
            value = TextFieldValue("test1111"),
            hintResId = R.string.modify_password_new_password,
            showWarning = false,
            onFocusChange = {},
        )
        Spacer(modifier = Modifier.padding(top = 18.dp))
        ModifyPasswordTextField(
            onValueChange = {},
            value = TextFieldValue("test11"),
            hintResId = R.string.modify_password_new_password_check,
            showWarning = true,
            onFocusChange = {}
        )
        ModifyPasswordWarning(
            warningResId = R.string.modify_password_new_passwords_mismatch_warning,
            bottomPadding = 67.dp,
            showWarning = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        FMButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp),
            onClick = {},
            text = stringResource(id = R.string.modify_password_btn),
            enabled = false
        )
    }
}
