package io.familymoments.app.feature.creatingfamily.screen

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.feature.choosingfamily.MemberCheckBox
import io.familymoments.app.feature.choosingfamily.ChoosingFamilyHeaderButtonLayout
import io.familymoments.app.feature.choosingfamily.SearchTextField
import io.familymoments.app.feature.creatingfamily.model.response.Member
import io.familymoments.app.feature.creatingfamily.viewmodel.CreatingFamilyViewModel

@Composable
fun SearchMemberScreen(
    navigate: () -> Unit = {},
    viewModel: CreatingFamilyViewModel
) {
    val context = LocalContext.current
    val searchMemberUiState = viewModel.searchMemberUiState.collectAsStateWithLifecycle()
    if (searchMemberUiState.value.isSuccess == false) {
        showErrorMessage(context, searchMemberUiState.value.errorMessage)
    }
    SearchMemberScreen(
        navigate = navigate,
        searchMember = viewModel::searchMember,
        members = searchMemberUiState.value.members,
    )
}

fun showErrorMessage(context: Context, errorMessage: String?) {
    Toast.makeText(
        context,
        errorMessage,
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun SearchMemberScreen(
    navigate: () -> Unit = {},
    searchMember: (String) -> Unit,
    members: List<Member>
) {

    ChoosingFamilyHeaderButtonLayout(
        headerBottomPadding = 34.dp,
        header = stringResource(id = R.string.select_create_family_header),
        button = stringResource(id = R.string.next_btn_one_third),
        onClick = navigate
    ) {
        Column {
            SearchMemberTextField(searchMember)
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                MemberList(members)
            }
            Spacer(modifier = Modifier.height(29.dp))
        }
    }
}

@Composable
private fun SearchMemberTextField(
    searchMember: (String) -> Unit
) {
    var value by remember {
        mutableStateOf(TextFieldValue(""))
    }
    searchMember(value.text)
    SearchTextField(
        hint = stringResource(id = R.string.member_search_text_field_hint)
    ) {
        value = it
    }
}

@Composable
private fun MemberList(members: List<Member>) {
    var selectedMembers: List<Member> by remember {
        mutableStateOf(listOf())
    }
    LazyColumn {
        members.forEach {
            item {
                MemberItem(
                    profileImg = it.profileImg,
                    id = it.id,
                    status = it.status,
                    onChecked = {
                        selectedMembers = selectedMembers + it
                    },
                    onUnChecked = {
                        selectedMembers = selectedMembers - it
                    }
                )
                Divider(
                    color = AppColors.grey3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }
    }
}

@Composable
private fun MemberItem(
    profileImg: String,
    id: String,
    status: Int,
    onChecked: () -> Unit,
    onUnChecked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = if (status == 1) AppColors.grey6 else AppColors.grey3)
            .padding(horizontal = 15.dp, vertical = 8.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(end = 15.dp)
                .clip(CircleShape)
                .size(48.dp),
            model = profileImg,
            contentDescription = null
        )
        Text(text = id, style = AppTypography.B2_14, color = Color(0xFF1B1A57), modifier = Modifier.weight(1f))
        if (status == 1) {
            MemberCheckBox(
                modifier = Modifier
                    .size(28.dp),
                initChecked = false,
                onCheckChanged = {
                    if (it) onChecked() else onUnChecked()
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SearchMemberScreenPreview() {
    FamilyMomentsTheme {
        SearchMemberScreen(searchMember = {}, members = listOf())
    }
}

@Preview(showBackground = true)
@Composable
fun MemberListPreview() {
    FamilyMomentsTheme {
        MemberList(
            members = listOf(
                Member("a", "", 1),
                Member("b", "", 0),
                Member("c", "", 1)
            )
        )
    }
}
