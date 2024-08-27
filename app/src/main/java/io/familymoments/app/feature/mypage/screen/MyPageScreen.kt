package io.familymoments.app.feature.mypage.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.mypage.MyPageItem
import io.familymoments.app.feature.mypage.component.LogoutPopup

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    onItemClick: (clickedItem: MyPageItem) -> Unit
) {
    val showLogoutPopup = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        MyPageTitle()
        MyPageGroup(
            groupNameResId = R.string.my_page_account_group,
            myPageItems = MyPageGroups.accountGroup,
            onItemClick = onItemClick
        )
        val activity = LocalContext.current as Activity

        MyPageGroup(
            groupNameResId = R.string.my_page_more_group,
            myPageItems = MyPageGroups.moreGroup,
            onItemClick = onItemClick,
            onLogoutItemClick = { showLogoutPopup.value = true },
            onShowServiceTerms = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.route))
                activity.startActivity(intent)
            },
            onShowPrivacyPolicy = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.route))
                activity.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.padding(bottom = 27.dp))
    }
    if (showLogoutPopup.value) {
        LogoutPopup(
            onDismissRequest = { showLogoutPopup.value = false },
            viewModel = hiltViewModel()
        )
    }
}

@Composable
fun MyPageTitle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.my_page_title),
            style = AppTypography.B1_16,
            color = AppColors.black1,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 18.dp)
        )
    }
}

@Composable
fun MyPageGroup(
    @StringRes groupNameResId: Int,
    myPageItems: List<MyPageItem>,
    onItemClick: (clickedItem: MyPageItem) -> Unit,
    onLogoutItemClick: (clickedItem: MyPageItem) -> Unit = {},
    onShowServiceTerms: (clickedItem: MyPageItem) -> Unit = {},
    onShowPrivacyPolicy: (clickedItem: MyPageItem) -> Unit = {}
) {
    Column {
        MyPageGroupHeader(groupNameResId = groupNameResId)
        myPageItems.forEach { item ->
            when (item.route) {
                MyPageItem.Logout.route -> MyPageGroupItem(item = item, onItemClick = onLogoutItemClick)
                MyPageItem.ServiceTerms.route -> MyPageGroupItem(item = item, onItemClick = onShowServiceTerms)
                MyPageItem.PrivacyPolicy.route -> MyPageGroupItem(item = item, onItemClick = onShowPrivacyPolicy)
                else -> MyPageGroupItem(item = item, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun MyPageGroupHeader(
    @StringRes groupNameResId: Int,
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 16.dp, bottom = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(AppColors.grey8)
        ) {
            Text(
                text = stringResource(id = groupNameResId),
                style = AppTypography.SH2_18,
                color = AppColors.grey6,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun MyPageGroupItem(
    item: MyPageItem,
    onItemClick: (clickedItem: MyPageItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .padding(end = 4.dp)
            .clickable { onItemClick(item) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(45.dp)
        ) {
            Icon(
                painter = painterResource(id = item.iconResId),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 2.dp)
            )
        }
        Text(
            text = stringResource(id = item.labelResId),
            style = AppTypography.B1_16,
            color = AppColors.black1,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .size(32.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_my_page_right_arrow),
                contentDescription = "navigate to ${item.route}",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private object MyPageGroups {
    val accountGroup = listOf(
        MyPageItem.Profile,
        MyPageItem.Password,
        MyPageItem.Notification,
        MyPageItem.FamilyInvitationList,
        MyPageItem.FamilySettings
    )
    val moreGroup = listOf(
        MyPageItem.ServiceTerms,
        MyPageItem.PrivacyPolicy,
        MyPageItem.Logout,
        MyPageItem.AccountDeletion
    )
}

@Preview(showBackground = true)
@Composable
fun MyPageScreenPreview() {
    MyPageScreen(onItemClick = { })
}
