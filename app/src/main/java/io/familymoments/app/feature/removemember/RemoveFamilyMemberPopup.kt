package io.familymoments.app.feature.removemember

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.familymoments.app.R
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.noRippleClickable

@Composable
fun RemoveFamilyMemberPopup(
    onDismissRequest: () -> Unit = {},
    onDoneButtonClicked: () -> Unit = {},
    nicknames: List<String> = emptyList()
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(AppColors.grey6),
        ) {
            Image(
                modifier = Modifier
                    .noRippleClickable { onDismissRequest() }
                    .align(Alignment.TopEnd)
                    .padding(top = 14.dp, end = 14.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_album_popup_close),
                contentDescription = "close popup",
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 37.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.remove_family_member_popup_content_1),
                    style = AppTypography.BTN4_18,
                    color = AppColors.grey8,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = AppTypography.SH1_20.toSpanStyle()) {
                            append(nicknames.joinToString(separator = ", "))
                        }
                        withStyle(style = AppTypography.BTN4_18.toSpanStyle()) {
                            append(stringResource(id = R.string.remove_family_member_popup_content_2))
                        }
                    },
                    color = AppColors.grey8,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 21.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 17.dp)
                        .padding(bottom = 12.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    FMButton(
                        modifier = Modifier.weight(1f).height(54.dp),
                        onClick = onDismissRequest,
                        text = stringResource(id = R.string.remove_family_member_popup_cancel),
                        containerColor = AppColors.purple1
                    )
                    Spacer(modifier = Modifier.width(22.dp))
                    FMButton(
                        modifier = Modifier.weight(1f).height(54.dp),
                        onClick = onDoneButtonClicked,
                        text = stringResource(id = R.string.remove_family_member_popup_done),
                        containerColor = AppColors.purple2
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemoveFamilyMemberPopupPreview() {
    RemoveFamilyMemberPopup(nicknames = listOf("엠마", "클로이"))
}
