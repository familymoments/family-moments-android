package io.familymoments.app.feature.joiningfamily.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.choosingfamily.component.ChoosingFamilyHeaderButtonLayout
import io.familymoments.app.feature.choosingfamily.component.MemberCheckBox
import io.familymoments.app.feature.choosingfamily.component.SearchTextField
import io.familymoments.app.feature.joiningfamily.viewmodel.JoinFamilyViewModel


@Composable
fun JoinFamilyScreen(
    viewModel: JoinFamilyViewModel,
    navigate: () -> Unit = {}
) {
    var inviteLinkTextFieldValue by remember {
        mutableStateOf(TextFieldValue())
    }
    var selectedFamilyId by remember {
        mutableStateOf<Long?>(null)
    }
    val joinFamilyUiState = viewModel.joinFamilyUiState.collectAsStateWithLifecycle().value
    val searchFamilyByInviteLinkUiState = joinFamilyUiState.searchFamilyByInviteLinkUiState
    val joinFamilyExecuteUiState = joinFamilyUiState.joinFamilyExecuteUiState
    val context = LocalContext.current
    val joinFamilySuccessMessage = stringResource(R.string.join_family_success_message)

    LaunchedEffect(inviteLinkTextFieldValue) {
        viewModel.searchFamilyByInviteLink(inviteLinkTextFieldValue.text)
    }
    LaunchedEffect(joinFamilyExecuteUiState.isSuccess) {
        if (joinFamilyExecuteUiState.isSuccess == true) {
            Toast.makeText(context, joinFamilySuccessMessage, Toast.LENGTH_SHORT).show()
            navigate()
        } else if (joinFamilyExecuteUiState.isSuccess == false) {
            Toast.makeText(context, joinFamilyExecuteUiState.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.resetJoinFamilyExecuteSuccess()
        }
    }
    ChoosingFamilyHeaderButtonLayout(
        headerBottomPadding = 18.dp,
        header = stringResource(R.string.header_join_family),
        button = stringResource(R.string.button_family_join_now),
        buttonEnabled = selectedFamilyId != null,
        onClick = {
            viewModel.joinFamily(familyId = selectedFamilyId ?: throw IllegalStateException())
        }
    ) {
        Column {
            SearchTextField(
                singleLine = true,
                hint = stringResource(R.string.family_invitation_link_text_field_hint)
            ) { inviteLinkTextFieldValue = it }
            Spacer(modifier = Modifier.height(36.dp))
            if (searchFamilyByInviteLinkUiState.isSuccess == true) {
                if (searchFamilyByInviteLinkUiState.result != null)
                    Box(modifier = Modifier.background(color = AppColors.grey6)) {
                        FamilyProfile(
                            searchFamilyByInviteLinkUiState.result
                        ) { selectedFamilyId = it }
                    }
            }

        }

    }
}

@Composable
private fun FamilyProfile(
    searchFamilyByInviteLinkResult: io.familymoments.app.core.network.dto.response.SearchFamilyByInviteLinkResult,
    setSelectedFamilyId: (Long?) -> Unit
) {
    var checked by remember {
        mutableStateOf(false)
    }
    if (checked) {
        setSelectedFamilyId(searchFamilyByInviteLinkResult.familyId)
    } else {
        setSelectedFamilyId(null)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp)
    ) {
        AsyncImage(
            model = searchFamilyByInviteLinkResult.representImg,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 15.dp)
                .clip(CircleShape)
                .size(48.dp)
        )
        Text(text = searchFamilyByInviteLinkResult.familyName, style = AppTypography.B2_14, color = Color(0xFF1B1A57))
        MemberCheckBox(
            modifier = Modifier
                .weight(1f)
                .size(28.dp),
            onCheckChanged = { checked = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJoinScreen() {
    JoinFamilyScreen(hiltViewModel())
}
