package io.familymoments.app.feature.creatingfamily.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.util.FileUtil.convertUriToBitmap

@Composable
fun GalleryOrDefaultImageSelectButton(
    context: Context,
    getImageBitmap: (Bitmap?) -> Unit
) {

    val defaultImageBitmap =
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.default_profile
        )
    var bitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    getImageBitmap(bitmap)

    val launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it == null) return@rememberLauncherForActivityResult
        bitmap = convertUriToBitmap(it, context)
    }

    var isMenuExpanded: Boolean by remember { mutableStateOf(false) }
    Button(
        modifier = Modifier.height(192.dp),
        onClick = { isMenuExpanded = !isMenuExpanded },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppColors.grey5,
        ),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
    ) {
        val imageHeight = 192
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null
                )
            } else {
                Image(
                    modifier = Modifier.padding(bottom = 2.dp),
                    painter = painterResource(id = R.drawable.ic_select_pic),
                    contentDescription = null,
                )
                Text(text = stringResource(R.string.join_select_profile_image_btn), color = AppColors.grey3)
            }
            GalleryOrDefaultImageSelectDropDownMenu(
                launcher = launcher,
                getDefaultImageBitmap = { bitmap = defaultImageBitmap },
                isMenuExpanded = isMenuExpanded
            ) {
                isMenuExpanded = false
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryOrDefaultImageSelectButtonPreview() {
    val context = LocalContext.current
    GalleryOrDefaultImageSelectButton(context) {}
}
