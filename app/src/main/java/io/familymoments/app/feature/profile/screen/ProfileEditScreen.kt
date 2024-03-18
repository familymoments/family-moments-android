package io.familymoments.app.feature.profile.screen

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.component.FMDropdownMenu
import io.familymoments.app.core.component.FMTextField
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.profile.model.uistate.ProfileImage
import io.familymoments.app.feature.profile.viewmodel.ProfileEditViewModel

@Composable
fun ProfileEditScreen(
    navigateBack: () -> Unit,
    viewModel: ProfileEditViewModel,
) {
    val defaultProfileImageBitmap = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.default_profile)
    val profileEditUiState = viewModel.profileEditUiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.edit_profile_title),
            style = AppTypography.B1_16,
            color = AppColors.black1,
            modifier = Modifier.padding(bottom = 18.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier.padding(top = 4.dp)
            ) {
                when (profileEditUiState.value.profileImg) {
                    is ProfileImage.Url -> {
                        AsyncImage(
                            model = (profileEditUiState.value.profileImg as ProfileImage.Url).imgUrl,
                            contentDescription = "profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                    }
                    is ProfileImage.Bitmap -> {
                        Image(
                            bitmap = (profileEditUiState.value.profileImg as ProfileImage.Bitmap).bitmap.asImageBitmap(),
                            contentDescription = "profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
        FMDropdownMenu(
            onGallerySelected = { Log.d("DropdownMenu", "갤러리 선택") },
            onDefaultImageSelected = { Log.d("DropdownMenu", "기본이미지") }
        )
        Spacer(modifier = Modifier.height(34.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            ProfileTextField(
                modifier = Modifier.padding(bottom = 42.dp),
                title = stringResource(id = R.string.profile_text_field_name),
                value = profileEditUiState.value.profile.name,
                hint = "이름 입력"
            )
            ProfileTextField(
                modifier = Modifier.padding(bottom = 42.dp),
                title = stringResource(id = R.string.profile_text_field_nickname),
                value = profileEditUiState.value.profile.nickname,
                hint = "닉네임 입력"
            )
            ProfileTextField(
                modifier = Modifier.padding(bottom = 31.dp),
                title = stringResource(id = R.string.profile_text_field_birth_date),
                value = profileEditUiState.value.profile.birthdate,
                hint = "생년월일 입력"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 11.dp, end = 11.dp),
            ) {
                Button(
                    onClick = { navigateBack() },
                    modifier = Modifier
                        .weight(1f)
                        .size(54.dp),
                    colors = ButtonDefaults.buttonColors(AppColors.purple1, Color.White),
                    shape = RoundedCornerShape(60.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.profile_btn_cancel),
                        style = AppTypography.BTN4_18,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = 34.dp))
                Button(
                    onClick = { navigateBack() },
                    modifier = Modifier
                        .weight(1f)
                        .size(54.dp),
                    colors = ButtonDefaults.buttonColors(AppColors.purple2, Color.White),
                    shape = RoundedCornerShape(60.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.profile_btn_done),
                        style = AppTypography.BTN4_18,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    hint: String
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value)) }
    Text(
        text = title,
        style = AppTypography.B1_16,
        color = AppColors.deepPurple1,
        modifier = Modifier.padding(start = 1.dp, bottom = 2.dp)
    )
    FMTextField(
        modifier = modifier,
        value = textFieldValue,
        onValueChange = { newValue -> textFieldValue = newValue },
        hint = hint,
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileEditScreenPreview() {
    ProfileEditScreen(
        navigateBack = {},
        hiltViewModel()
    )
}
