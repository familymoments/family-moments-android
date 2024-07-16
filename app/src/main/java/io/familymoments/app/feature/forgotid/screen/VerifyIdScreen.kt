package io.familymoments.app.feature.forgotid.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.forgotid.viewmodel.VerifyIdViewModel

@Composable
fun VerifyIdScreen(viewModel: VerifyIdViewModel, navigate: (String) -> Unit) {
    var name by remember {
        mutableStateOf(TextFieldValue())
    }
    var email by remember {
        mutableStateOf(TextFieldValue())
    }
    var code by remember {
        mutableStateOf(TextFieldValue())
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    val sendEmailUiState = viewModel.uiState.collectAsStateWithLifecycle().value.sendEmailUiState
    val findIdUiState = viewModel.uiState.collectAsStateWithLifecycle().value.findIdUiState
    val context = LocalContext.current

    LaunchedEffect(sendEmailUiState) {
        isLoading = sendEmailUiState.isLoading
        if (sendEmailUiState.isSuccess == true) {
            Toast.makeText(context, sendEmailUiState.result, Toast.LENGTH_SHORT).show()
        } else if (sendEmailUiState.isSuccess == false) {
            Toast.makeText(context, sendEmailUiState.message, Toast.LENGTH_SHORT).show()
        }
        viewModel.resetSendEmailSuccess()
    }

    LaunchedEffect(findIdUiState) {
        if (findIdUiState.isSuccess == true) {
            navigate(findIdUiState.userId)
        } else if (findIdUiState.isSuccess == false) {
            Toast.makeText(context, findIdUiState.message, Toast.LENGTH_SHORT).show()
        }
        viewModel.resetFindIdSuccess()
    }

    LoadingIndicator(isLoading = isLoading)

    VerifyIdScreenUI(
        name = name,
        onNameChanged = { name = it },
        email = email,
        onEmailChanged = { email = it },
        code = code,
        onCodeChanged = { code = it },
        sendEmail = {
            viewModel.sendEmail(name.text, email.text)
        },
        findId = {
            viewModel.findId(name.text, email.text, code.text)
        }
    )
}

@Composable
fun VerifyIdScreenUI(
    name: TextFieldValue,
    onNameChanged: (TextFieldValue) -> Unit,
    email: TextFieldValue,
    onEmailChanged: (TextFieldValue) -> Unit,
    code: TextFieldValue,
    onCodeChanged: (TextFieldValue) -> Unit,
    sendEmail: () -> Unit,
    findId: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(83.dp))
        Text(
            text = stringResource(id = R.string.login_forgot_id),
            style = AppTypography.H1_36,
            color = AppColors.grey8
        )
        Spacer(modifier = Modifier.height(13.dp))
        Text(
            text = stringResource(R.string.forgot_id_verify_email_label),
            style = AppTypography.LB1_13,
            color = AppColors.grey2
        )
        Spacer(modifier = Modifier.height(67.dp))
        Text(
            text = stringResource(R.string.forgot_id_name_tf_title),
            style = AppTypography.SH2_18,
            color = AppColors.grey2
        )
        Spacer(modifier = Modifier.height(17.dp))
        FMTextField(
            onValueChange = onNameChanged,
            value = name,
            hint = stringResource(R.string.forgot_id_name_tf_hint),
            showDeleteButton = true
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(R.string.forgot_id_email_tf_title),
            style = AppTypography.SH2_18,
            color = AppColors.grey2
        )
        Spacer(modifier = Modifier.height(17.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FMTextField(
                modifier = Modifier
                    .weight(1f),
                onValueChange = onEmailChanged,
                value = email,
                hint = stringResource(R.string.forgot_id_email_tf_hint)
            )
            Spacer(modifier = Modifier.width(7.dp))
            FMButton(
                onClick = sendEmail,
                text = stringResource(R.string.forgot_id_send_email_btn),
                radius = 10.dp,
                textStyle = AppTypography.BTN5_16,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        FMTextField(
            onValueChange = onCodeChanged,
            value = code,
            hint = stringResource(R.string.forgot_id_code_tf_hint),
            showDeleteButton = true
        )
        Spacer(modifier = Modifier.height(33.dp))
        FMButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = findId,
            text = stringResource(R.string.next_btn),
            contentPaddingValues = PaddingValues(top = 20.dp, bottom = 19.dp)
        )
    }

}

@Composable
@Preview(showBackground = true)
fun VerifyIdScreenPreview() {
    var name by remember {
        mutableStateOf(TextFieldValue())
    }
    var email by remember {
        mutableStateOf(TextFieldValue())
    }
    var code by remember {
        mutableStateOf(TextFieldValue())
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        VerifyIdScreenUI(
            name = name,
            onNameChanged = { name = it },
            email = email,
            onEmailChanged = { email = it },
            code = code,
            onCodeChanged = { code = it },
            sendEmail = {},
            findId = {}
        )
    }

}
