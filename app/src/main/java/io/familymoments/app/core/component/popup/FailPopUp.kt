package io.familymoments.app.core.component.popup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
fun WarningPopup(
    content: String = "네트워크 상태가 좋지 않습니다.",
    positiveBtnLabel: String = "확인",
    negativeBtnLabel: String = "취소",
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
                        .fillMaxWidth()
                ) {
                    Image(
                        modifier = Modifier
                            .noRippleClickable { onDismissRequest() }
                            .align(Alignment.TopEnd)
                            .padding(top = 14.dp, end = 14.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_album_popup_close),
                        contentDescription = "close popup",
                    )

                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 17.dp, bottom = 16.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_warning),
                        contentDescription = "warning"
                    )
                }
                Text(
                    text = content,
                    style = AppTypography.BTN4_18,
                    color = AppColors.grey8,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 30.dp),
                    textAlign = TextAlign.Center
                )
                Row {
                    Button(
                        modifier = Modifier
                            .width(252.dp)
                            .weight(1f)
                            .padding(bottom = 21.dp, end = 11.dp, start = 17.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.grey3),
                        shape = RoundedCornerShape(60.dp),
                        onClick = onDismissRequest,
                        contentPadding = PaddingValues(top = 17.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = negativeBtnLabel,
                            style = AppTypography.BTN4_18,
                            color = AppColors.grey6
                        )
                    }
                    Button(
                        modifier = Modifier
                            .width(252.dp)
                            .weight(1f)
                            .padding(bottom = 21.dp, start = 11.dp, end = 17.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.red1),
                        shape = RoundedCornerShape(60.dp),
                        onClick = onDismissRequest,
                        contentPadding = PaddingValues(top = 17.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = positiveBtnLabel,
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
fun WarningPopupPreview() {
    WarningPopup()
}
