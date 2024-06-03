package io.familymoments.app.feature.forgotpassword.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.feature.forgotpassword.uistate.FindPwdUiState
import io.familymoments.app.feature.forgotpassword.uistate.SendEmailUiState
import io.familymoments.app.feature.forgotpassword.viewmodel.VerifyViewModel

@Composable
fun VerifyScreen(
    verifyViewModel: VerifyViewModel,
    navigate: () -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var certifyNumber by remember { mutableStateOf(TextFieldValue()) }
    val uiState = verifyViewModel.uiState.collectAsStateWithLifecycle().value
    val sendEmailUiState = uiState.sendEmailUiState
    val findPwdUiState = uiState.findPwdUiState
    val context = LocalContext.current

    LaunchedEffectWithSendEmailUiState(context, sendEmailUiState, verifyViewModel::resetSendEmailSuccess)
    LaunchedEffectWithFindPwdUiState(context, findPwdUiState, verifyViewModel::resetFindPwdSuccess) { navigate() }

    VerifyScreenUI(
        name = name,
        email = email,
        certifyNumber = certifyNumber,
        onNameChanged = { name = it },
        onEmailChanged = { email = it },
        onCertifyNumberChanged = { certifyNumber = it },
        sendEmail = { verifyViewModel.sendEmail(name.text, email.text) },
        verify = { verifyViewModel.findPwd(name.text, email.text, certifyNumber.text) }
    )
}

@Composable
fun LaunchedEffectWithSendEmailUiState(
    context: Context,
    uiState: SendEmailUiState,
    resetSuccess: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    LoadingIndicator(isLoading = isLoading)

    LaunchedEffect(uiState.isLoading) {
        isLoading = uiState.isLoading
    }
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess == true) {
            Toast.makeText(context, uiState.result, Toast.LENGTH_SHORT).show()
        } else if (uiState.isSuccess == false) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        }
        resetSuccess()
    }
}

@Composable
fun LaunchedEffectWithFindPwdUiState(
    context: Context,
    uiState: FindPwdUiState,
    resetSuccess: () -> Unit,
    navigate: () -> Unit,
) {
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess == true) {
            Toast.makeText(context, uiState.result, Toast.LENGTH_SHORT).show()
            navigate()
        } else if (uiState.isSuccess == false) {
            Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
        }
        resetSuccess()
    }
}

@Composable
fun VerifyScreenUI(
    name: TextFieldValue,
    email: TextFieldValue,
    certifyNumber: TextFieldValue,
    onNameChanged: (TextFieldValue) -> Unit,
    onEmailChanged: (TextFieldValue) -> Unit,
    onCertifyNumberChanged: (TextFieldValue) -> Unit,
    sendEmail: () -> Unit,
    verify: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(68.dp))
            Text(
                text = stringResource(id = R.string.forgot_password_title_01),
                style = AppTypography.H1_36,
                color = AppColors.deepPurple1
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.forgot_password_desc_01),
                style = AppTypography.LB1_13,
                color = AppColors.grey2
            )
            Spacer(modifier = Modifier.height(67.dp))

            Text(
                text = stringResource(id = R.string.forgot_password_tf_name_title),
                style = AppTypography.SH2_18,
                color = AppColors.grey2
            )
            Spacer(modifier = Modifier.height(17.dp))
            FMTextField(
                value = name,
                onValueChange = onNameChanged,
                hint = stringResource(id = R.string.forgot_password_tf_name_hint),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(id = R.string.email),
                style = AppTypography.SH2_18,
                color = AppColors.grey2
            )
            Spacer(modifier = Modifier.height(17.dp))
            Row {
                FMTextField(
                    value = email,
                    onValueChange = onEmailChanged,
                    hint = stringResource(id = R.string.forgot_password_tf_email_hint),
                    modifier = Modifier
                        .weight(1f),
                )
                FMButton(
                    onClick = { sendEmail() },
                    text = stringResource(id = R.string.forgot_password_btn_cert),
                    modifier = Modifier.padding(start = 8.dp),
                    radius = 10.dp,
                    textStyle = AppTypography.BTN5_16
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            FMTextField(
                value = certifyNumber,
                onValueChange = onCertifyNumberChanged,
                hint = stringResource(id = R.string.forgot_password_tf_cn_hint),
            )

            Spacer(modifier = Modifier.height(33.dp))
            FMButton(
                onClick = verify,
                text = stringResource(id = R.string.next),
                contentPaddingValues = PaddingValues(top = 20.dp, bottom = 18.dp),
                modifier = Modifier.fillMaxWidth(),
                textModifier = Modifier
            )
            Spacer(modifier = Modifier.height(101.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerifyScreenUIPreview() {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var certifyNumber by remember { mutableStateOf(TextFieldValue()) }
    FamilyMomentsTheme {
        VerifyScreenUI(
            name = name,
            email = email,
            certifyNumber = certifyNumber,
            onNameChanged = { name = it },
            onEmailChanged = { email = it },
            onCertifyNumberChanged = { certifyNumber = it },
            sendEmail = {},
            verify = {}
        )
    }
}
