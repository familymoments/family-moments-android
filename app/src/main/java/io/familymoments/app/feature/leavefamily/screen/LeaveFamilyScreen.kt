package io.familymoments.app.feature.leavefamily.screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.popup.CompletePopUp
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.choosingfamily.activity.ChoosingFamilyActivity
import io.familymoments.app.feature.leavefamily.uistate.LeaveFamilyUiState
import io.familymoments.app.feature.leavefamily.viewmodel.LeaveFamilyViewModel

@Composable
fun LeaveFamilyScreen(
    modifier: Modifier = Modifier,
    viewModel: LeaveFamilyViewModel,
    navigateBack: () -> Unit,
) {
    var showPermissionPopup by remember { mutableStateOf(false) }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(uiState.isOwner) {
        showPermissionPopup = uiState.isOwner
    }
    LaunchedEffectLeaveFamily(uiState, context, viewModel::resetSuccess)

    LeaveFamilyScreenUI(
        modifier = modifier,
        leaveFamily = viewModel::leaveFamily,
        navigateBack = navigateBack
    )

    if (showPermissionPopup) {
        CompletePopUp(
            content = stringResource(id = R.string.leave_family_popup_content),
            dismissText = stringResource(id = R.string.leave_family_popup_btn),
            textStyle = AppTypography.BTN5_16,
            buttonColors = ButtonDefaults.buttonColors(containerColor = AppColors.purple2),
            onDismissRequest = {
                showPermissionPopup = false
                navigateBack()
            }
        )
    }
}

@Composable
fun LeaveFamilyScreenUI(
    modifier: Modifier = Modifier,
    leaveFamily: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.leave_family_title),
                style = AppTypography.B1_16,
                color = AppColors.black1,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 18.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = stringResource(id = R.string.leave_family_content_1),
                    style = AppTypography.B1_16,
                    color = AppColors.grey8,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.leave_family_content_2),
                    style = AppTypography.SH1_20,
                    color = AppColors.grey8,
                    modifier = Modifier
                        .padding(top = 35.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
        Button(
            onClick = leaveFamily,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 20.dp)
                .height(59.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.pink1, contentColor = AppColors.grey6)
        ) {
            Text(
                text = stringResource(id = R.string.leave_family_done_btn),
                style = AppTypography.BTN4_18
            )
        }
        Button(
            onClick = navigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 95.dp)
                .height(59.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.grey8, contentColor = AppColors.grey6)
        ) {
            Text(
                text = stringResource(id = R.string.leave_family_cancel_btn),
                style = AppTypography.BTN4_18
            )
        }
    }
}

@Composable
fun LaunchedEffectLeaveFamily(
    uiState: LeaveFamilyUiState,
    context: Context,
    resetSuccess: () -> Unit = {}
) {
    LaunchedEffect(uiState.isSuccess) {
        uiState.isSuccess?.let { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, R.string.leave_family_complete, Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ChoosingFamilyActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            } else {
                Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                resetSuccess()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaveFamilyScreenPreview() {
    LeaveFamilyScreenUI()
}
