package io.familymoments.app.feature.forgotpassword.screen

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.component.FMTextField
import io.familymoments.app.core.component.LoadingIndicator
import io.familymoments.app.core.component.popup.CompletePopUp
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.forgotpassword.uistate.ResetUiState
import io.familymoments.app.feature.forgotpassword.viewmodel.ForgotPasswordSharedViewModel
import io.familymoments.app.feature.forgotpassword.viewmodel.ResetViewModel

@Composable
fun ResetScreen(forgotPasswordSharedViewModel: ForgotPasswordSharedViewModel, resetViewModel: ResetViewModel) {

    val context = LocalContext.current
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordConfirm by remember { mutableStateOf(TextFieldValue()) }
    var isValid by remember { mutableStateOf(false) }
    val uiState = resetViewModel.uiState.collectAsStateWithLifecycle().value
    val id = forgotPasswordSharedViewModel.id.collectAsStateWithLifecycle().value

    LaunchedEffectWithUiState(context, uiState, resetViewModel::showDialog) { isValid = it }
    ResetScreenUI(
        password = password,
        passwordConfirm = passwordConfirm,
        onPasswordChanged = {
            password = it
            resetViewModel.updatePassword(password.text)
            resetViewModel.checkPasswordValid()
        },
        onPasswordConfirmChanged = {
            passwordConfirm = it
            resetViewModel.updatePasswordConfirm(passwordConfirm.text)
            resetViewModel.checkPasswordValid()
        },
        isValid = isValid,
        modifyPwd = { resetViewModel.modifyPwd(id) }
    )
}

@Composable
fun LaunchedEffectWithUiState(
    context: Context,
    uiState: ResetUiState,
    showDialog: () -> Unit,
    onValidChanged: (Boolean) -> Unit,
) {
    var isLoading by remember { mutableStateOf(false) }
    var dialog by remember { mutableStateOf(false) }

    if (isLoading) {
        LoadingIndicator(isLoading = true)
    }
    if (dialog) {
        CompletePopUp(
            content = stringResource(R.string.modify_pwd_in_find_pwd_dialog_content),
            dismissText = stringResource(R.string.modify_pwd_in_find_pwd_dialog_dismiss)
        ) { (context as Activity).finish() }
    }

    LaunchedEffect(uiState.isLoading) {
        isLoading = uiState.isLoading
    }
    LaunchedEffect(uiState.showDialog) {
        dialog = uiState.showDialog
    }
    LaunchedEffect(uiState.isValid) {
        onValidChanged(uiState.isValid)
    }
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess == true) {
            showDialog()
        } else if (uiState.isSuccess == false) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun ResetScreenUI(
    password: TextFieldValue,
    passwordConfirm: TextFieldValue,
    onPasswordChanged: (TextFieldValue) -> Unit,
    onPasswordConfirmChanged: (TextFieldValue) -> Unit,
    isValid: Boolean,
    modifyPwd: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(67.dp))
        Text(
            text = stringResource(id = R.string.forgot_password_title_02),
            style = AppTypography.H1_36,
            color = AppColors.deepPurple1
        )
        Spacer(modifier = Modifier.height(31.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.forgot_password_desc_02),
            style = AppTypography.B1_16,
            color = AppColors.deepPurple1
        )

        Spacer(modifier = Modifier.height(31.dp))
        FMTextField(
            value = password,
            onValueChange = onPasswordChanged,
            hint = stringResource(id = R.string.forgot_password_tf_pw_hint),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppColors.pink5)
        )
        Spacer(modifier = Modifier.height(16.dp))
        FMTextField(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppColors.pink5),
            value = passwordConfirm,
            onValueChange = onPasswordConfirmChanged,
            hint = stringResource(id = R.string.forgot_password_tf_pw_confirm_hint),
        )
        Spacer(modifier = Modifier.height(9.dp))
        if (passwordConfirm.text.isNotEmpty() && !isValid) {
            Text(
                text = stringResource(id = R.string.sign_up_password_check_validation_warning),
                style = AppTypography.LB1_13,
                color = AppColors.red2
            )
        }

        Spacer(modifier = Modifier.height(201.dp))
        Spacer(modifier = Modifier.weight(1f))
        FMButton(
            onClick = modifyPwd,
            text = stringResource(id = R.string.forgot_password_btn_reset),
            contentPaddingValues = PaddingValues(top = 20.dp, bottom = 18.dp),
            modifier = Modifier.fillMaxWidth(),
            textModifier = Modifier,
            enabled = isValid
        )
        Spacer(modifier = Modifier.height(101.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ResetScreenUIPreview() {
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordConfirm by remember { mutableStateOf(TextFieldValue()) }
    ResetScreenUI(
        password = password,
        passwordConfirm = passwordConfirm,
        onPasswordChanged = { password = it },
        onPasswordConfirmChanged = { passwordConfirm = it },
        isValid = false,
        modifyPwd = {}
    )

}
