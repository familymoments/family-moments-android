package io.familymoments.app.feature.deleteaccount.screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.component.popup.CompletePopUp
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.deleteaccount.viewmodel.DeleteAccountViewModel
import io.familymoments.app.feature.login.activity.LoginActivity

@Composable
fun DeleteAccountScreen(
    viewModel: DeleteAccountViewModel, navigateToBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var showCompletePopup by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(uiState.showPopup) {
        showCompletePopup = uiState.showPopup
    }
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess == false) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.resetSuccess()
        }
    }
    if (showCompletePopup) {
        CompletePopUp(
            content = stringResource(R.string.account_delete_popup_label),
            buttonColors = ButtonDefaults.buttonColors(containerColor = AppColors.purple2)
        ) {
            viewModel.resetPopup()
            navigateToLogin(context)
        }
    }

    DeleteAccountScreenUI(viewModel::deleteAccount, navigateToBack)

}

fun navigateToLogin(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}

@Composable
fun DeleteAccountScreenUI(deleteAccount: () -> Unit = {}, navigateToBack: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 13.dp)) {
        Spacer(modifier = Modifier.height(18.dp))
        DeleteAccountHeader()
        Spacer(modifier = Modifier.height(141.dp))
        DeleteAccountContent(deleteAccount, navigateToBack)
    }
}

@Composable
private fun DeleteAccountHeader() {
    Text(
        text = stringResource(id = R.string.my_page_label_account_deletion),
        style = AppTypography.B1_16,
        color = AppColors.black1
    )
}

@Composable
private fun DeleteAccountContent(deleteAccount: () -> Unit, navigateToBack: () -> Unit) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = 3.dp),
            text = stringResource(R.string.account_deletion_notice_text),
            style = AppTypography.BTN4_18,
            color = AppColors.grey8
        )
        Spacer(modifier = Modifier.height(58.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.account_deletion_farewell_text),
            style = AppTypography.BTN3_20,
            color = AppColors.grey8,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(103.dp))
        FMButton(
            onClick = deleteAccount,
            text = stringResource(R.string.account_deletion_delete_btn),
            containerColor = AppColors.pink1,
            modifier = Modifier.fillMaxWidth(),
            contentPaddingValues = PaddingValues(vertical = 18.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        FMButton(
            onClick = navigateToBack,
            text = stringResource(R.string.account_deletion_cancel_btn),
            containerColor = AppColors.grey8,
            modifier = Modifier.fillMaxWidth(),
            contentPaddingValues = PaddingValues(vertical = 18.dp)
        )
    }

}

@Composable
@Preview(showBackground = true)
private fun DeleteAccountScreenPreview() {
    DeleteAccountScreenUI()
}
