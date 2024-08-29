package io.familymoments.app.feature.profile.screen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.component.FMTextField
import io.familymoments.app.core.component.ImageSelectionMenu
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.URI_SCHEME_RESOURCE
import io.familymoments.app.core.util.noRippleClickable
import io.familymoments.app.feature.profile.viewmodel.ProfileEditViewModel
import java.io.File

@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: ProfileEditViewModel,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val defaultProfileImageUri =
        Uri.parse("$URI_SCHEME_RESOURCE://${context.packageName}/${R.drawable.default_profile}")
    var nickname by remember { mutableStateOf(TextFieldValue(uiState.value.nickname)) }

    LaunchedEffect(uiState.value.isSuccess) {
        if (uiState.value.isSuccess) {
            navigateBack()
        }
    }

    ProfileEditScreenUI(
        modifier = modifier,
        context = context,
        nickname = nickname,
        defaultProfileImageUri = defaultProfileImageUri,
        profileImage = uiState.value.profileImage,
        onImageChanged = viewModel::imageChanged,
        onNicknameChanged = {
            nickname = it
            viewModel.validateNickname(it.text)
        },
        nicknameValidated = uiState.value.nicknameValidated,
        onEditButtonClicked = {
            onEditButtonClicked(
                context = context,
                profileImage = uiState.value.profileImage,
                nickname = nickname.text,
                editUserProfile = viewModel::editUserProfile
            )
        },
        navigateBack = navigateBack
    )
}

@Composable
private fun ProfileEditScreenUI(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    nickname: TextFieldValue = TextFieldValue(),
    nicknameValidated: Boolean = true,
    defaultProfileImageUri: Uri,
    profileImage: File?,
    onImageChanged: (Context, Uri) -> Unit = { _, _ -> },
    onNicknameChanged: (TextFieldValue) -> Unit = {},
    onEditButtonClicked: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.edit_profile_title),
            style = AppTypography.B1_16,
            color = AppColors.black1,
            modifier = Modifier.padding(vertical = 18.dp)
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 22.dp)
        ) {
            AsyncImage(
                model = profileImage,
                contentDescription = "profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center)
            )
        }
        EditImageDialog(
            context = context,
            defaultProfileImageUri = defaultProfileImageUri,
            onImageChanged = onImageChanged,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 34.dp),
            horizontalAlignment = Alignment.Start
        ) {
            ProfileTextField(
                title = stringResource(id = R.string.profile_text_field_nickname),
                hint = stringResource(id = R.string.profile_text_field_hint_nickname),
                warning = stringResource(id = R.string.profile_nickname_warning),
                showWarning = !nicknameValidated,
                value = nickname,
                onValueChanged = onNicknameChanged
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 96.dp)
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
                onClick = onEditButtonClicked,
                enabled = nicknameValidated,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.purple2,
                    contentColor = AppColors.grey6,
                    disabledContainerColor = AppColors.grey3,
                    disabledContentColor = AppColors.grey6
                ),
                stringResId = R.string.profile_btn_done
            )
        }
    }
}

private fun onEditButtonClicked(
    context: Context,
    profileImage: File?,
    nickname: String,
    editUserProfile: (File, String) -> Unit
) {
    if (profileImage == null) {
        Toast.makeText(context, R.string.profile_edit_image_error, Toast.LENGTH_SHORT).show()
    } else {
        editUserProfile(profileImage, nickname)
    }
}

@Composable
private fun ProfileTextField(
    title: String = "",
    hint: String = "",
    warning: String = "",
    showWarning: Boolean = false,
    value: TextFieldValue = TextFieldValue(),
    onValueChanged: (TextFieldValue) -> Unit = {},
) {
    Column {
        Text(
            text = title,
            style = AppTypography.B1_16,
            color = AppColors.grey8,
            modifier = Modifier.padding(start = 1.dp, bottom = 2.dp)
        )
        FMTextField(
            value = value,
            onValueChange = onValueChanged,
            hint = hint,
            textColor = if (showWarning) AppColors.red2 else AppColors.black2,
            hintColor = if (showWarning) AppColors.red2 else AppColors.grey2,
            borderColor = if (showWarning) AppColors.red2 else AppColors.grey2
        )
        if (showWarning) {
            Text(
                modifier = Modifier
                    .height(42.dp)
                    .padding(top = 3.dp),
                text = warning,
                style = AppTypography.LB2_11,
                color = AppColors.red2
            )
        } else {
            Spacer(modifier = Modifier.height(42.dp))
        }
    }
}

@Composable
private fun EditImageDialog(
    context: Context = LocalContext.current,
    defaultProfileImageUri: Uri,
    onImageChanged: (Context, Uri) -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it == null) return@rememberLauncherForActivityResult
            onImageChanged(context, it)
        }
    )
    var showImageSelectionMenu by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.profile_select_photo),
            style = AppTypography.B1_16,
            color = AppColors.purple2,
            modifier = Modifier
                .align(Alignment.Center)
                .noRippleClickable { showImageSelectionMenu = true }
        )
    }
    ImageSelectionMenu(
        showImageSelectionMenu = showImageSelectionMenu,
        onDismissRequest = { showImageSelectionMenu = false },
        onGallerySelected = {
            launcher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onDefaultImageSelected = {
            onImageChanged(context, defaultProfileImageUri)
        }
    )
}

@Composable
private fun ProfileButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    colors: ButtonColors,
    @StringRes stringResId: Int,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(54.dp),
        enabled = enabled,
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
private fun ProfileEditScreenPreview() {
    ProfileEditScreenUI(
        defaultProfileImageUri = Uri.parse(""),
        profileImage = null,
    )
}
