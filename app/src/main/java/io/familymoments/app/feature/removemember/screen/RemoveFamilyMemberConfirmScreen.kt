package io.familymoments.app.feature.removemember.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.removemember.viewmodel.RemoveFamilyMemberConfirmViewModel

@Composable
fun RemoveFamilyMemberConfirmScreen(
    modifier: Modifier = Modifier,
    viewModel: RemoveFamilyMemberConfirmViewModel,
    navigateBack: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, R.string.remove_family_member_complete, Toast.LENGTH_SHORT).show()
            navigateBack(); navigateBack()
        }
    }

    RemoveFamilyMemberConfirmScreenUI(
        modifier = modifier,
        navigateBack = navigateBack,
        onDoneButtonClicked = viewModel::removeFamilyMember
    )
}

@Composable
fun RemoveFamilyMemberConfirmScreenUI(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    onDoneButtonClicked: () -> Unit = {}
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.remove_family_member_title),
                style = AppTypography.B1_16,
                color = AppColors.black1,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 18.dp)
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = buildString {
                        append(stringResource(id = R.string.remove_family_member_confirm_content_1))
                        append(stringResource(id = R.string.remove_family_member_confirm_content_2))
                    },
                    style = AppTypography.B1_16,
                    color = AppColors.grey8,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.remove_family_member_confirm_content_3),
                    style = AppTypography.SH1_20,
                    color = AppColors.grey8,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 31.dp)
                )
            }
        }
        FMButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .height(59.dp),
            onClick = onDoneButtonClicked,
            text = stringResource(id = R.string.remove_family_member_confirm_continue_btn),
            containerColor = AppColors.pink1
        )
        FMButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 95.dp)
                .height(59.dp),
            onClick = navigateBack,
            text = stringResource(id = R.string.remove_family_member_confirm_cancel_btn),
            containerColor = AppColors.grey8
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RemoveFamilyMemberConfirmScreenPreview() {
    RemoveFamilyMemberConfirmScreenUI()
}
