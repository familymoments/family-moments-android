package io.familymoments.app.core.component.popup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.noRippleClickable

@Composable
fun DeletePopUp(
    content: String,
    delete: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
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
                    text = content,
                    style = AppTypography.BTN4_18,
                    color = AppColors.grey8,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 23.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 17.dp)
                        .padding(bottom = 12.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.pink1),
                        shape = RoundedCornerShape(60.dp),
                        onClick = delete,
                        contentPadding = PaddingValues(top = 17.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.post_delete_pop_up_btn_delete),
                            style = AppTypography.BTN4_18,
                            color = AppColors.grey6
                        )
                    }
                    Spacer(modifier = Modifier.width(22.dp))
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.purple1),
                        shape = RoundedCornerShape(60.dp),
                        onClick = onDismissRequest,
                        contentPadding = PaddingValues(top = 17.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.post_delete_pop_up_btn_cancel),
                            style = AppTypography.BTN4_18,
                            color = AppColors.grey6
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailExecutePopUpPreview() {
    DeletePopUp("댓글을\n삭제하시겠습니까?")
}
