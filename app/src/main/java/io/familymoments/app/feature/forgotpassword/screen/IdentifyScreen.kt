package io.familymoments.app.feature.forgotpassword.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.component.FMTextField
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.forgotpassword.uistate.IdentifyUiState
import io.familymoments.app.feature.forgotpassword.viewmodel.ForgotPasswordSharedViewModel
import io.familymoments.app.feature.forgotpassword.viewmodel.IdentifyViewModel


@Composable
fun IdentifyScreen(
    forgotPasswordSharedViewModel: ForgotPasswordSharedViewModel,
    identifyViewModel: IdentifyViewModel,
    navigate: () -> Unit
) {
    var id by remember { mutableStateOf(TextFieldValue()) }
    val uiState = identifyViewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffectWithSuccess(
        context = context,
        uiState = uiState,
        resetSuccess = identifyViewModel::resetSuccess,
        onSuccess = {
            forgotPasswordSharedViewModel.updateId(id.text)
            navigate()
        }
    )

    IdentifyScreenUI(
        id = id,
        onIdChanged = { id = it },
        checkIdExist = { identifyViewModel.checkIdExist(id.text) }
    )
}

@Composable
fun LaunchedEffectWithSuccess(
    context: Context,
    uiState: IdentifyUiState,
    resetSuccess: () -> Unit,
    onSuccess: () -> Unit
) {
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess == false) {
            val message = uiState.message.ifEmpty {
                context.getString(R.string.forgot_password_screen_check_id_default_error_message)
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            resetSuccess()
        } else if (uiState.isSuccess == true) {
            onSuccess()
        }
    }
}

@Composable
fun IdentifyScreenUI(
    id: TextFieldValue,
    onIdChanged: (TextFieldValue) -> Unit,
    checkIdExist: () -> Unit
) {

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(68.dp))
        Text(
            text = stringResource(id = R.string.forgot_password_title_01),
            style = AppTypography.H1_36,
            color = AppColors.grey8
        )
        Spacer(modifier = Modifier.height(13.dp))
        Text(
            text = stringResource(id = R.string.forgot_password_desc_01),
            style = AppTypography.LB1_13,
            color = AppColors.grey2
        )
        Spacer(modifier = Modifier.height(61.dp))

        FMTextField(
            value = id,
            onValueChange = onIdChanged,
            hint = stringResource(id = R.string.forgot_password_tf_id_hint),
            modifier = Modifier
                .width(240.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(253.dp))
        FMButton(
            onClick = checkIdExist,
            text = stringResource(id = R.string.next),
            modifier = Modifier.fillMaxWidth(),
            contentPaddingValues = PaddingValues(top = 20.dp, bottom = 18.dp),
        )
        Spacer(modifier = Modifier.height(101.dp))
    }
}
