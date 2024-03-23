package io.familymoments.app.feature.profile.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import io.familymoments.app.core.util.convertUriToBitmap
import io.familymoments.app.core.util.urlToBitmap
import io.familymoments.app.feature.profile.model.uistate.ProfileImage
import io.familymoments.app.feature.profile.viewmodel.ProfileEditViewModel

@Composable
fun ProfileEditScreen(
    navigateBack: () -> Unit,
    viewModel: ProfileEditViewModel,
) {
    val defaultProfileImageBitmap =
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.default_profile)
    val profileEditUiState = viewModel.profileEditUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(profileEditUiState.value.profileImage) {
        if (profileEditUiState.value.profileImage is ProfileImage.Url) {
            val bitmap = urlToBitmap((profileEditUiState.value.profileImage as ProfileImage.Url).imgUrl, context)
            viewModel.imageChanged(bitmap)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.edit_profile_title),
            style = AppTypography.B1_16,
            color = AppColors.black1,
            modifier = Modifier.padding(bottom = 18.dp)
        )
        ProfileImageRenderer(profileImage = profileEditUiState.value.profileImage)
        Spacer(modifier = Modifier.height(22.dp))
        ProfileDropdown(
            defaultProfileImageBitmap = defaultProfileImageBitmap,
            onImageChanged = viewModel::imageChanged,
        )
        Spacer(modifier = Modifier.height(34.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 31.dp),
            horizontalAlignment = Alignment.Start
        ) {
            ProfileTextField(
                modifier = Modifier.padding(bottom = 42.dp),
                title = stringResource(id = R.string.profile_text_field_name),
                value = profileEditUiState.value.profileEditInfoUiState.name,
                hint = stringResource(id = R.string.profile_text_field_hint_name),
                onValueChanged = viewModel::nameChanged
            )
            ProfileTextField(
                modifier = Modifier.padding(bottom = 42.dp),
                title = stringResource(id = R.string.profile_text_field_nickname),
                value = profileEditUiState.value.profileEditInfoUiState.nickname,
                hint = stringResource(id = R.string.profile_text_field_hint_nickname),
                onValueChanged = viewModel::nicknameChanged
            )
            ProfileTextField(
                modifier = Modifier.padding(bottom = 31.dp),
                title = stringResource(id = R.string.profile_text_field_birth_date),
                value = profileEditUiState.value.profileEditInfoUiState.birthdate,
                hint = stringResource(id = R.string.profile_text_field_hint_birth_date),
                onValueChanged = viewModel::birthdateChanged
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 11.dp),
        ) {
            ProfileButton(
                modifier = Modifier.weight(1f),
                onClick = navigateBack,
                colors = ButtonDefaults.buttonColors(AppColors.purple1, Color.White),
                stringResId = R.string.profile_btn_cancel
            )
            Spacer(modifier = Modifier.width(34.dp))
            ProfileButton(
                modifier = Modifier.weight(1f),
                onClick = { viewModel.requestEditProfile(context, navigateBack) },
                colors = ButtonDefaults.buttonColors(AppColors.purple2, Color.White),
                stringResId = R.string.profile_btn_done
            )
        }
    }
}

@Composable
fun ProfileImageRenderer(
    profileImage: ProfileImage,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.padding(top = 4.dp)
        ) {
            when (profileImage) {
                is ProfileImage.Url -> {
                    AsyncImage(
                        model = profileImage.imgUrl,
                        contentDescription = "profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                }

                is ProfileImage.Bitmap -> {
                    Image(
                        bitmap = profileImage.bitmap.asImageBitmap(),
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
}

@Composable
private fun ProfileTextField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    hint: String,
    onValueChanged: (String) -> Unit
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
        onValueChange = { newValue ->
            textFieldValue = newValue
            onValueChanged(newValue.text)
        },
        hint = hint,
    )
}

@Composable
fun ProfileDropdown(
    defaultProfileImageBitmap: Bitmap,
    onImageChanged: (Bitmap?) -> Unit,
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it == null) return@rememberLauncherForActivityResult
            onImageChanged(convertUriToBitmap(it, context))
        }
    )
    FMDropdownMenu(
        onGallerySelected = {
            launcher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onDefaultImageSelected = {
            onImageChanged(defaultProfileImageBitmap)
        }
    )
}

@Composable
fun ProfileButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: ButtonColors,
    @StringRes stringResId: Int,
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .size(54.dp),
        colors = colors,
        shape = RoundedCornerShape(60.dp)
    ) {
        Text(
            text = stringResource(id = stringResId),
            style = AppTypography.BTN4_18,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditScreenPreview() {
    ProfileEditScreen(
        navigateBack = {},
        hiltViewModel()
    )
}
