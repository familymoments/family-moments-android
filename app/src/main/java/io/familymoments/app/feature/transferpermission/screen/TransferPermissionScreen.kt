package io.familymoments.app.feature.transferpermission.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.component.popup.CompletePopUp
import io.familymoments.app.core.network.dto.response.Member
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.noRippleClickable
import io.familymoments.app.feature.transferpermission.viewmodel.TransferPermissionViewModel

@Composable
fun TransferPermissionScreen(
    modifier: Modifier,
    viewModel: TransferPermissionViewModel,
    navigateBack: () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var selectedMember by remember { mutableStateOf<Member?>(null) }
    var showCompletePopup by remember { mutableStateOf(false) }
    var showPermissionPopup by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        showCompletePopup = uiState.isSuccess
    }
    LaunchedEffect(uiState.isOwner) {
        showPermissionPopup = !uiState.isOwner
    }

    TransferPermissionScreenUI(
        modifier = modifier,
        members = uiState.members,
        onSelectedMemberChanged = { selectedMember = it },
        selectedMember = selectedMember,
        onDoneButtonClicked = viewModel::transferPermission
    )

    TransferPermissionPopup(
        showCompletePopup = showCompletePopup,
        showPermissionPopup = showPermissionPopup,
        completePopupDismissRequest = {
            showCompletePopup = false
            navigateBack()
        },
        permissionPopupDismissRequest = {
            showPermissionPopup = false
            navigateBack()
        }
    )
}

@Composable
fun TransferPermissionScreenUI(
    modifier: Modifier = Modifier,
    members: List<Member>,
    selectedMember: Member? = null,
    onSelectedMemberChanged: (Member?) -> Unit = {},
    onDoneButtonClicked: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 55.dp, bottom = 33.dp),
            text = stringResource(id = R.string.transfer_permission_title),
            style = AppTypography.SH2_18,
            color = AppColors.grey8
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = members, key = { it.id }) { member ->
                FamilyMember(
                    member = member,
                    onSelectedMemberChanged = onSelectedMemberChanged,
                    selectedMember = selectedMember
                )
                HorizontalDivider(thickness = 1.dp, color = AppColors.grey3)
            }
        }
        FMButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 29.dp, bottom = 95.dp)
                .height(59.dp),
            enabled = selectedMember != null,
            onClick = { onDoneButtonClicked(selectedMember!!.id) },
            text = stringResource(id = R.string.transfer_permission_title),
            containerColor = AppColors.grey8
        )
    }
}

@Composable
fun FamilyMember(
    member: Member,
    onSelectedMemberChanged: (Member?) -> Unit,
    selectedMember: Member?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp)
            .noRippleClickable {
                onSelectedMemberChanged(
                    if (selectedMember == member) null else member
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = member.profileImg,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 15.dp)
                .clip(CircleShape)
                .size(48.dp)
        )
        Text(text = member.id, style = AppTypography.B2_14, color = AppColors.black1)
        Box(
            modifier = Modifier
                .weight(1f)
                .size(28.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                painter = painterResource(id = if (selectedMember == member) R.drawable.ic_round_checked else R.drawable.ic_round_unchecked),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
private fun TransferPermissionPopup(
    showCompletePopup: Boolean,
    showPermissionPopup: Boolean,
    completePopupDismissRequest: () -> Unit = {},
    permissionPopupDismissRequest: () -> Unit = {}
) {
    if (showCompletePopup) {
        CompletePopUp(
            content = stringResource(id = R.string.transfer_permission_complete_popup_content),
            dismissText = stringResource(id = R.string.transfer_permission_complete_popup_btn),
            buttonColors = ButtonDefaults.buttonColors(containerColor = AppColors.purple2),
            onDismissRequest = completePopupDismissRequest
        )
    }
    if (showPermissionPopup) {
        CompletePopUp(
            content = stringResource(id = R.string.check_family_permission_popup_content),
            dismissText = stringResource(id = R.string.check_family_permission_popup_btn),
            buttonColors = ButtonDefaults.buttonColors(containerColor = AppColors.purple2),
            onDismissRequest = permissionPopupDismissRequest
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransferPermissionScreenPreview() {
    TransferPermissionScreenUI(members = emptyList())
}
