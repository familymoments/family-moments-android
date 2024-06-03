package io.familymoments.app.feature.familysettings.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.scaffoldState
import io.familymoments.app.feature.familysettings.FamilySettingNavItem

@Composable
fun FamilySettingScreen(
    onItemClick: (clickedItem: FamilySettingNavItem) -> Unit
) {
    Column(
        modifier = Modifier
            .scaffoldState(
                hasShadow = true,
                hasBackButton = true
            )
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.family_setting_title),
                style = AppTypography.B1_16,
                color = AppColors.black1,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 18.dp)
            )
        }
        FamilySetting.items.forEach { item ->
            FamilySettingItem(item = item, onItemClick = onItemClick)
        }
    }
}

@Composable
fun FamilySettingItem(
    item: FamilySettingNavItem,
    onItemClick: (clickedItem: FamilySettingNavItem) -> Unit
) {
    val modifier = if (item == FamilySettingNavItem.DeleteFamily) Modifier.padding(top = 56.dp) else Modifier
    val color = if (item == FamilySettingNavItem.DeleteFamily) AppColors.red1 else AppColors.black1
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .clickable { onItemClick(item) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(47.dp)
                .fillMaxHeight()
        ) {
            Icon(
                painter = painterResource(id = item.iconResId),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                tint = color
            )
        }
        Text(
            text = stringResource(id = item.labelResId),
            style = AppTypography.B1_16,
            color = color,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_my_page_right_arrow),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                tint = color
            )
        }
    }
}

private object FamilySetting {
    val items = listOf(
        FamilySettingNavItem.ModifyFamilyInfo,
        FamilySettingNavItem.FamilyInvitationLink,
        FamilySettingNavItem.AddFamilyMember,
        FamilySettingNavItem.ChangeUploadCycle,
        FamilySettingNavItem.TransferFamilyPermission,
        FamilySettingNavItem.RemoveFamilyMember,
        FamilySettingNavItem.LeaveFamily,
        FamilySettingNavItem.DeleteFamily
    )
}

@Preview(showBackground = true)
@Composable
fun FamilySettingScreenPreview() {
    FamilySettingScreen(onItemClick = {})
}
