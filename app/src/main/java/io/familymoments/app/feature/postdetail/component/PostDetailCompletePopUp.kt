package io.familymoments.app.feature.postdetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
fun PostDetailCompletePopUp(
    content: String,
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(AppColors.grey6),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.End)
                        .padding(top = 14.dp, end = 14.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .noRippleClickable { onDismissRequest() }
                            .align(Alignment.Center),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_album_popup_close),
                        contentDescription = "close popup",
                    )
                }
                Text(
                    text = content,
                    style = AppTypography.BTN4_18,
                    color = AppColors.deepPurple1,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp, bottom = 30.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    modifier = Modifier
                        .width(252.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 21.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.pink1),
                    shape = RoundedCornerShape(60.dp),
                    onClick = onDismissRequest,
                    contentPadding = PaddingValues(top = 17.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.complete_pop_up_btn_ok),
                        style = AppTypography.BTN4_18,
                        color = AppColors.grey6
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailCompletePopUpPopUpPreview() {
    PostDetailCompletePopUp("삭제가 완료되었습니다.")
}
