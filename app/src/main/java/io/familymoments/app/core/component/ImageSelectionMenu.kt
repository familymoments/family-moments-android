package io.familymoments.app.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.noRippleClickable

@Composable
fun ImageSelectionMenu(
    onDismissRequest: () -> Unit,
    onGallerySelected: () -> Unit,
    onDefaultImageSelected: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 9.dp)
                .padding(bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .noRippleClickable { onDismissRequest() }
            )
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppColors.grey6)
            ) {
                TextButton(
                    onClick = {
                        onGallerySelected()
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(59.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.edit_image_gallery_select),
                        style = AppTypography.BTN4_18,
                        color = AppColors.grey9
                    )
                }
                HorizontalDivider(color = AppColors.grey9, thickness = 1.dp)
                TextButton(
                    onClick = {
                        onDefaultImageSelected()
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(59.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.edit_image_default_image),
                        style = AppTypography.BTN4_18,
                        color = AppColors.grey9
                    )
                }
            }
            Spacer(modifier = Modifier.height(11.dp))
            Button(
                onClick = onDismissRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(59.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(AppColors.grey6)
            ) {
                Text(
                    text = stringResource(id = R.string.edit_image_cancel),
                    style = AppTypography.BTN3_20,
                    color = AppColors.grey9
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionSheetPreview() {
    ImageSelectionMenu(
        onDismissRequest = {},
        onGallerySelected = {},
        onDefaultImageSelected = {}
    )
}
