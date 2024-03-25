package io.familymoments.app.feature.creatingfamily.screen

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.component.GalleryOrDefaultImageSelectButton
import io.familymoments.app.feature.choosingfamily.ChoosingFamilyHeaderButtonLayout
import io.familymoments.app.feature.creatingfamily.model.FamilyProfile
import io.familymoments.app.feature.creatingfamily.viewmodel.CreatingFamilyViewModel

@Composable
fun SetProfileScreen(
    viewModel: CreatingFamilyViewModel,
    navigate: () -> Unit
) {
    val context = LocalContext.current
    var familyName by remember {
        mutableStateOf("")
    }
    var familyImg by remember {
        mutableStateOf<Bitmap?>(null)
    }
    Column {
        ChoosingFamilyHeaderButtonLayout(
            headerBottomPadding = 29.dp,
            header = stringResource(id = R.string.select_create_family_header),
            button = stringResource(id = R.string.next_btn_two_third),
            onClick = {
                viewModel.saveFamilyProfile(FamilyProfile(familyName, familyImg))
                navigate()
            }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SetUpFamilyName {
                    familyName = it.text
                }
                Spacer(modifier = Modifier.height(20.dp))
                SetUpFamilyPicture(context) { familyImg = it }
            }
        }

    }
}

@Composable
fun SetUpFamilyName(
    onValueChanged: (TextFieldValue) -> Unit
) {
    var familyName by remember {
        mutableStateOf(TextFieldValue())
    }
    onValueChanged(familyName)
    Text(
        text = stringResource(R.string.select_family_name),
        style = AppTypography.B1_16,
        color = AppColors.black1
    )
    Spacer(modifier = Modifier.height(4.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.grey5, shape = RoundedCornerShape(7.dp))
            .padding(vertical = 12.dp, horizontal = 11.dp),
    ) {
        BasicTextField(
            value = familyName,
            onValueChange = { familyName = it },
            textStyle = AppTypography.LB1_13.copy(color = AppColors.black1)
        ) { innerTextField ->
            if (familyName.text.isEmpty()) {
                Text(
                    text = stringResource(R.string.family_name_text_field_hint),
                    style = AppTypography.LB1_13,
                    color = AppColors.grey2
                )
            }
            innerTextField()
        }
    }

}

@Composable
fun SetUpFamilyPicture(context: Context, onBitmapChanged: (Bitmap?) -> Unit) {

    Text(
        text = stringResource(R.string.select_family_image),
        style = AppTypography.B1_16,
        color = AppColors.black1
    )
    Spacer(modifier = Modifier.height(4.dp))
    GalleryOrDefaultImageSelectButton(
        context = context,
        getImageBitmap = {
            onBitmapChanged(it)
        }

    )
}


@Preview(showBackground = true)
@Composable
fun PreviewSetProfileScreen() {
    SetProfileScreen(hiltViewModel()) {}
}
