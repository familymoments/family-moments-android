package io.familymoments.app.feature.postdetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.noRippleClickable

@Composable
fun ReportPopUp(
    onDismissRequest: () -> Unit = {},
    onReportRequest: (String) -> Unit = {},
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .width(250.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppColors.grey6),
        ) {
            Column(
                modifier = Modifier.wrapContentWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        text = stringResource(R.string.report_pop_up_label_reason_for_report),
                        style = AppTypography.B1_16,
                        color = AppColors.black2,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 16.dp, start = 16.dp)
                    )
                    Image(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 14.dp, end = 14.dp)
                            .noRippleClickable { onDismissRequest() },
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_album_popup_close),
                        contentDescription = "close popup",
                    )
                }
                ReportItems(Modifier.padding(top = 42.dp, bottom = 13.dp))

                var reportContents by remember {
                    mutableStateOf(TextFieldValue())
                }

                Box(
                    modifier = Modifier
                        .padding(start = 19.dp, end = 19.dp, bottom = 13.dp)
                        .border(width = 1.dp, color = AppColors.pink1)
                        .fillMaxWidth()
                        .heightIn(103.dp)
                        .padding(10.dp)
                ) {
                    BasicTextField(
                        value = reportContents,
                        onValueChange = {
                            reportContents = it
                        },
                        textStyle = AppTypography.LB2_11
                    ) { innerTextField ->
                        innerTextField()
                    }
                }
                Text(
                    text = stringResource(R.string.report_pop_up_label_warning),
                    style = AppTypography.LB2_11,
                    color = AppColors.grey2,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.pink1),
                    shape = RoundedCornerShape(60.dp),
                    onClick = {
                        onReportRequest(reportContents.text)
                    },
                    modifier = Modifier
                        .padding(start = 26.dp, end = 26.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(top = 9.59.dp, bottom = 10.41.dp)
                ) {
                    Text(
                        text = stringResource(R.string.report_pop_up_btn_report),
                        style = AppTypography.BTN5_16,
                        color = AppColors.grey6
                    )
                }
            }
        }
    }
}

@Composable
fun ReportItems(modifier: Modifier) {
    LazyVerticalGrid(
        modifier = modifier.then(Modifier.padding(start = 19.dp, end = 19.dp)),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(19.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val contents = listOf(
            "영리목적/홍보성", "저작권 침해",
            "음란성/선정성", "욕설/인신공격",
            "같은내용 반복게시", "개인정보노출",
            "기타"

        )
        items(contents) {
            ReportItem(it)
        }


    }
}

@Composable
fun ReportItem(content: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        var checkStatus by remember {
            mutableStateOf(false)
        }
        Icon(
            imageVector =
            if (checkStatus) ImageVector.vectorResource(R.drawable.ic_pink_circle_check)
            else ImageVector.vectorResource(R.drawable.ic_pink_circle_uncheck),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.noRippleClickable {
                checkStatus = !checkStatus
            }
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = content,
            style = AppTypography.LB2_11
        )
    }

}

@Preview(showBackground = true)
@Composable
fun ReportPopUpPreview() {
    ReportPopUp()
}
