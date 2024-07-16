package io.familymoments.app.feature.removemember.screen

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
import io.familymoments.app.feature.removemember.RemoveFamilyMemberPopup
import io.familymoments.app.feature.removemember.viewmodel.RemoveFamilyMemberViewModel

@Composable
fun RemoveFamilyMemberScreen(
    modifier: Modifier = Modifier,
    viewModel: RemoveFamilyMemberViewModel,
    navigateBack: () -> Unit = {},
    navigateToConfirmScreen: (List<String>) -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var selectedMembers by remember { mutableStateOf(listOf<Member>()) }
    var showPermissionPopup by remember { mutableStateOf(false) }
    var showRemovePopup by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isOwner) {
        showPermissionPopup = !uiState.isOwner
    }

    RemoveFamilyMemberScreenUI(
        modifier = modifier,
        members = uiState.members,
        selectedMembers = selectedMembers,
        onSelectedMemberChanged = {
            selectedMembers = if (selectedMembers.contains(it)) {
                selectedMembers - it
            } else {
                selectedMembers + it
            }
        },
        onDoneButtonClicked = { showRemovePopup = true }
    )

    RemoveFamilyMemberPopups(
        showPermissionPopup = showPermissionPopup,
        showRemovePopup = showRemovePopup,
        nicknames = selectedMembers.map { it.nickname },
        userIds = selectedMembers.map { it.id },
        permissionPopupDismissRequest = {
            showPermissionPopup = false
            navigateBack()
        },
        removePopupDismissRequest = {
            showRemovePopup = false
        },
        navigateToConfirmScreen = navigateToConfirmScreen
    )
}

@Composable
fun RemoveFamilyMemberScreenUI(
    modifier: Modifier = Modifier,
    members: List<Member> = emptyList(),
    selectedMembers: List<Member> = emptyList(),
    onSelectedMemberChanged: (Member) -> Unit = {},
    onDoneButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 55.dp, bottom = 33.dp),
            text = stringResource(id = R.string.remove_family_member_title),
            style = AppTypography.SH2_18,
            color = AppColors.grey8
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = members, key = { it.id }) {
                FamilyMember(
                    member = it,
                    onSelectedMemberChanged = onSelectedMemberChanged,
                    selectedMembers = selectedMembers
                )
                HorizontalDivider(thickness = 1.dp, color = AppColors.grey3)
            }
        }
        FMButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 29.dp, bottom = 95.dp)
                .height(59.dp),
            enabled = selectedMembers.isNotEmpty(),
            onClick = onDoneButtonClicked,
            text = stringResource(id = R.string.remove_family_member_btn),
            containerColor = AppColors.grey8
        )
    }
}

@Composable
fun FamilyMember(
    member: Member,
    onSelectedMemberChanged: (Member) -> Unit,
    selectedMembers: List<Member>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp)
            .noRippleClickable {
                onSelectedMemberChanged(member)
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
                painter = painterResource(id = if (selectedMembers.contains(member)) R.drawable.ic_round_checked else R.drawable.ic_round_unchecked),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
private fun RemoveFamilyMemberPopups(
    showPermissionPopup: Boolean,
    showRemovePopup: Boolean,
    nicknames: List<String> = emptyList(),
    userIds: List<String> = emptyList(),
    permissionPopupDismissRequest: () -> Unit = {},
    removePopupDismissRequest: () -> Unit = {},
    navigateToConfirmScreen: (List<String>) -> Unit = {},
) {
    if (showPermissionPopup) {
        CompletePopUp(
            content = stringResource(id = R.string.check_family_permission_popup_content),
            dismissText = stringResource(id = R.string.check_family_permission_popup_btn),
            buttonColors = ButtonDefaults.buttonColors(containerColor = AppColors.purple2),
            onDismissRequest = permissionPopupDismissRequest
        )
    }
    if (showRemovePopup) {
        RemoveFamilyMemberPopup(
            nicknames = nicknames,
            onDismissRequest = removePopupDismissRequest,
            onDoneButtonClicked = {
                removePopupDismissRequest()
                navigateToConfirmScreen(userIds)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RemoveFamilyMemberScreenPreview() {
    RemoveFamilyMemberScreenUI()
}
