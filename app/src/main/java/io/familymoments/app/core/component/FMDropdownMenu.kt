package io.familymoments.app.core.component


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import kotlin.math.roundToInt

@Composable
fun FMDropdownMenu(
    onGallerySelected: () -> Unit,
    onDefaultImageSelected: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.profile_select_photo),
                style = AppTypography.B1_16,
                color = AppColors.purple2,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickableWithoutRipple { expanded = true }
            )
        }
        if (expanded) {
            Popup(
                alignment = Alignment.TopCenter,
                properties = PopupProperties(focusable = true),
                offset = IntOffset(0, with(LocalDensity.current) { 9.dp.toPx().roundToInt() }),
                onDismissRequest = { expanded = false }
            ) {
                Box(
                    modifier = Modifier.dropShadow()
                ) {
                    Column(
                        modifier = Modifier
                            .background(color = AppColors.grey6, shape = RoundedCornerShape(6.dp))
                            .width(186.dp)
                            .wrapContentHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp)
                                .clickable {
                                    onGallerySelected()
                                    expanded = false
                                }
                        ) {
                            Text(
                                text = stringResource(id = R.string.profile_gallery_select_photo),
                                style = AppTypography.LB2_11,
                                color = AppColors.black1,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp, vertical = 10.dp)
                            )
                        }
                        Divider(
                            color = AppColors.grey3,
                            thickness = 0.68.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp)
                                .clickable {
                                    onDefaultImageSelected()
                                    expanded = false
                                }
                        ) {
                            Text(
                                text = stringResource(id = R.string.profile_default_image_select),
                                style = AppTypography.LB2_11,
                                color = AppColors.black1,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CustomDropdownMenuPreview() {
    var selectedItem by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FMDropdownMenu(
            onGallerySelected = { selectedItem = "갤러리에서 선택" },
            onDefaultImageSelected = { selectedItem = "기본이미지로 하기" }
        )

        selectedItem?.let {
            Text(
                text = "Selected Item: $it",
                style = AppTypography.LB2_11,
                color = AppColors.black1,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
