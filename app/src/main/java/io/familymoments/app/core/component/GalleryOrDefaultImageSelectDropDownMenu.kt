package io.familymoments.app.core.component

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.familymoments.app.R

@Composable
fun GalleryOrDefaultImageSelectDropDownMenu(
    launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    getDefaultImageBitmap: () -> Unit,
    isMenuExpanded: Boolean,
    onDismissRequest: () -> Unit
) {
    GalleryOrDefaultImageSelectDropDownMenu(
        isMenuExpanded,
        {
            launcher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        { getDefaultImageBitmap() },
        onDismissRequest
    )
}

@Composable
fun GalleryOrDefaultImageSelectDropDownMenu(
    isMenuExpanded: Boolean,
    onGallerySelect: () -> Unit,
    onDefaultImgSelect: () -> Unit,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(expanded = isMenuExpanded, onDismissRequest = onDismissRequest) {
        DropdownMenuItem(onClick = {
            onGallerySelect()
            onDismissRequest()
        }) {
            Text(text = stringResource(id = R.string.drop_down_menu_item_gallery_select))
        }
        DropdownMenuItem(onClick = {
            onDefaultImgSelect()
            onDismissRequest()
        }) {
            Text(text = stringResource(id = R.string.drop_down_menu_item_default_image))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryOrDefaultImageSelectDropDownMenuPreview() {
    GalleryOrDefaultImageSelectDropDownMenu(true, {}, {}, {})
}
