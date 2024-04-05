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
import androidx.compose.runtime.MutableState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.component.FMTextField
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.login.activity.LoginActivity
import io.familymoments.app.feature.modifypassword.model.WarningType
import io.familymoments.app.feature.modifypassword.model.uistate.CurrentPasswordUiState
import io.familymoments.app.feature.modifypassword.model.uistate.NewPasswordCheckUiState
import io.familymoments.app.feature.modifypassword.model.uistate.NewPasswordUiState
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
            currentPasswordUiState = uiState.value.currentPasswordUiState,
            checkCurrentPassword = viewModel::checkCurrentPassword,
            onClearCurrentPassword = viewModel::onClearCurrentPassword,
        )
        NewPasswordField(
            newPassword = newPassword,
            newPasswordCheck = newPasswordCheck,
            newPasswordUiState = uiState.value.newPasswordUiState,
            newPasswordCheckUiState = uiState.value.newPasswordCheckUiState,
            checkPasswordFormat = viewModel::checkPasswordFormat,
            checkPasswordEqual = viewModel::checkPasswordEqual,
            onClearNewPasswords = viewModel::onClearNewPasswords,
            requester = requester,
        )
        ModifyPasswordButton(
            currentPasswordValid = uiState.value.currentPasswordUiState.isValidated,
            newPasswordValid = isNewPasswordValidated,
            requester = requester,
            onClick = {
                viewModel.updatePasswordUiState(
                    currentPassword.value.text,
                    newPassword.value.text,
                    newPasswordCheck.value.text
                )
                viewModel.requestModifyPassword()
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
        color = AppColors.deepPurple1,
        modifier = Modifier.padding(bottom = 20.dp)
    )
    Text(
        text = stringResource(id = R.string.modify_password_notification_2),
        style = AppTypography.B1_16,
        color = AppColors.deepPurple1
    )
}

@Composable
private fun CurrentPasswordField(
    currentPassword: MutableState<TextFieldValue>,
    currentPasswordUiState: CurrentPasswordUiState,
    checkCurrentPassword: (String) -> Unit,
    onClearCurrentPassword: () -> Unit
) {
    LaunchedEffect(currentPasswordUiState.isReset) {
        if (currentPasswordUiState.isReset) {
            currentPassword.value = TextFieldValue()
            onClearCurrentPassword()
        }
    }
    ModifyPasswordTextField(
        onValueChange = {
            currentPassword.value = it
            checkCurrentPassword(it.text)
        },
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
    newPassword: MutableState<TextFieldValue>,
    newPasswordCheck: MutableState<TextFieldValue>,
    newPasswordUiState: NewPasswordUiState,
    newPasswordCheckUiState: NewPasswordCheckUiState,
    checkPasswordFormat: (String) -> Unit,
    checkPasswordEqual: (String, String) -> Unit,
    onClearNewPasswords: () -> Unit,
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
    LaunchedEffect(newPasswordUiState.isReset, newPasswordCheckUiState.isReset) {
        if (newPasswordUiState.isReset && newPasswordCheckUiState.isReset) {
            newPassword.value = TextFieldValue()
            newPasswordCheck.value = TextFieldValue()
            onClearNewPasswords()
        }
    }
    ModifyPasswordTextField(
        onValueChange = {
            newPassword.value = it
            checkPasswordFormat(it.text)
            checkPasswordEqual(it.text, newPasswordCheck.value.text)
        },
        value = newPassword.value,
        hintResId = R.string.modify_password_new_password,
        showWarning = newPasswordUiState.showWarningBorder,
        onFocusChange = onFocusChange,
    )
    Spacer(modifier = Modifier.padding(top = 18.dp))
    ModifyPasswordTextField(
        onValueChange = {
            newPasswordCheck.value = it
            checkPasswordFormat(newPassword.value.text)
            checkPasswordEqual(newPassword.value.text, it.text)
        },
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
    ModifyPasswordScreen(viewModel = hiltViewModel())
}
