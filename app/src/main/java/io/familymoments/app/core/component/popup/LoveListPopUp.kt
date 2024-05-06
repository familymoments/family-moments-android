package io.familymoments.app.core.component.popup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.network.dto.response.GetPostLovesResult
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.noRippleClickable

@Composable
fun LoveListPopUp(
    postLoves: List<GetPostLovesResult>,
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .width(250.dp)
                .heightIn(400.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppColors.grey6),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.love_list_pop_up_label),
                        style = AppTypography.B1_16,
                        color = AppColors.black2,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 16.dp, top = 16.dp)
                    )
                    Image(
                        modifier = Modifier
                            .noRippleClickable { onDismissRequest() }
                            .align(Alignment.TopEnd)
                            .padding(top = 14.dp, end = 14.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_album_popup_close),
                        contentDescription = "close popup",
                    )
                }
                LazyColumn(modifier = Modifier.padding(vertical = 17.dp)) {
                    val modifier = Modifier.padding(horizontal = 13.dp, vertical = 6.dp)
                    if (postLoves.isNotEmpty()) {
                        items(postLoves.size) {
                            LoveListItem(postLoves[it], modifier)
                            HorizontalDivider(thickness = 0.75.dp, color = AppColors.grey3)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoveListItem(member: GetPostLovesResult, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AsyncImage(
            model = member.profileImg,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 11.5.dp)
                .size(36.dp)
                .clip(shape = CircleShape)
        )
        Text(
            text = member.nickname,
            style = AppTypography.LB1_13,
            color = Color(0xFF1B1A57)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoveListPopUpPreview() {
    LoveListPopUp(listOf())
}
