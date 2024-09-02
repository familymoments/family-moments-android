package io.familymoments.app.feature.signup.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.R
import io.familymoments.app.core.component.CheckedStatus
import io.familymoments.app.core.component.SignUpTextFieldArea
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.core.util.FileUtil
import io.familymoments.app.core.util.PRIVACY_POLICY_URL
import io.familymoments.app.core.util.SERVICE_TERM_URL
import io.familymoments.app.core.util.defaultBitmap
import io.familymoments.app.feature.signup.uistate.SignUpTermUiState
import java.io.File

@Composable
fun IdField(
    userIdFormatValidated: Boolean,
    userIdDuplicated: Boolean,
    checkIdFormat: (String) -> Unit,
    checkIdDuplication: (String) -> Unit,
    resetUserIdDuplicatedPass: () -> Unit,
    onValueChange: (String) -> Unit
) {
    var previousId by remember {
        mutableStateOf(TextFieldValue())
    }
    var isFocused by remember {
        mutableStateOf(false)
    }
    Column {
        SignUpTextFieldArea(
            modifier = Modifier.onFocusChanged {
                isFocused = it.isFocused
            },
            title = stringResource(R.string.sign_up_id_field_title),
            hint = stringResource(R.string.sign_up_id_field_hint),
            onValueChange = {
                if (previousId.text != it.text) {
                    onValueChange(it.text)
                    checkIdFormat(it.text)
                    resetUserIdDuplicatedPass()
                    previousId = it
                }
            },
            showCheckButton = true,
            checkButtonAvailable = userIdFormatValidated,
            onCheckButtonClick = {
                checkIdDuplication(it.text)
            },
            validated = if (userIdFormatValidated) userIdDuplicated else false,
            showWarningText = true,
            warningText = if (userIdFormatValidated) stringResource(id = R.string.sign_up_need_duplication_check_warning)
            else stringResource(id = R.string.sign_up_id_validation_warning),
            isFocused = isFocused
        )
    }
}

@Composable
fun NicknameField(
    default: String = "",
    nicknameFormatValidated: Boolean,
    checkNicknameFormat: (String) -> Unit,
    onTextFieldChange: (String) -> Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    var nickname by remember {
        mutableStateOf(TextFieldValue())
    }

    LaunchedEffect(Unit) {
        checkNicknameFormat(default)
    }

    Column {
        SignUpTextFieldArea(
            modifier = Modifier.onFocusChanged {
                isFocused = it.isFocused
            },
            title = stringResource(id = R.string.sign_up_nickname_field_title),
            hint = stringResource(R.string.sign_up_nickname_field_hint),
            initialValue = default,
            onValueChange = {
                nickname = it
                onTextFieldChange(it.text)
                checkNicknameFormat(it.text)
            },
            isFocused = isFocused,
            showWarningText = true,
            warningText = stringResource(id = R.string.sign_up_nickname_validation_warning),
            validated = nicknameFormatValidated
        )
    }
}

@Composable
fun ProfileImageSelectDropDownMenu(
    isMenuExpanded: Boolean,
    isMenuExpandedChanged: (Boolean) -> Unit,
    defaultProfileImageBitmap: Bitmap,
    getBitmap: (Bitmap, Boolean) -> Unit,
) {
    val context = LocalContext.current
    var bitmap by remember {
        mutableStateOf(defaultBitmap)
    }

    //갤러리에서 사진 선택 후 bitmap 으로 변환
    val launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it == null) return@rememberLauncherForActivityResult
        bitmap = FileUtil.convertUriToBitmap(it, context)
        getBitmap(bitmap, true)
    }

    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = { isMenuExpandedChanged(false) }
    ) {
        MenuItemGallerySelect(launcher, isMenuExpandedChanged)
        MenuItemDefaultImage(
            { getBitmap(defaultProfileImageBitmap, it) },
            isMenuExpandedChanged
        )
    }
}

@Composable
fun MenuItemGallerySelect(
    launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    isMenuExpandedChanged: (Boolean) -> Unit
) {
    DropdownMenuItem(
        onClick = {
            launcher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
            isMenuExpandedChanged(false)
        },
        text = {
            Text(text = stringResource(R.string.sign_up_select_profile_image_drop_down_menu_gallery))
        }
    )
}

@Composable
fun MenuItemDefaultImage(getDefaultProfileImageBitmap: (Boolean) -> Unit, isMenuExpandedChanged: (Boolean) -> Unit) {
    DropdownMenuItem(onClick = {
        getDefaultProfileImageBitmap(false)
        isMenuExpandedChanged(false)
    }, text = {
        Text(text = stringResource(R.string.sign_up_select_profile_image_drop_down_menu_default_image))
    })
}

@Composable
fun ProfileImageField(defaultProfileImageBitmap: Bitmap, context: Context, isDefaultImage: MutableState<Boolean> = mutableStateOf(false), onFileChange: (File?) -> Unit) {
    var isMenuExpanded: Boolean by remember { mutableStateOf(false) }
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    Text(
        text = stringResource(R.string.sign_up_select_profile_image_title),
        color = Color(0xFF5B6380),
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(7.dp))
    Button(
        modifier = Modifier.height(150.dp),
        onClick = { isMenuExpanded = !isMenuExpanded },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF3F4F7),
        ),
        shape = RoundedCornerShape(7.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
    ) {
        ProfileImageSelectDropDownMenu(
            isMenuExpanded,
            { isMenuExpanded = it },
            defaultProfileImageBitmap
        ) { image, needToResize ->
            if (needToResize) {
                onFileChange(FileUtil.convertBitmapToCompressedFile(image, context))
            } else {
                isDefaultImage.value = true
            }
            bitmap = image
        }
        if (bitmap == null) {
            if (isDefaultImage.value) {
                Image(
                    painter = painterResource(id = R.drawable.default_profile),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = Modifier.padding(bottom = 2.dp),
                        painter = painterResource(id = R.drawable.ic_select_pic),
                        contentDescription = null,
                    )
                    Text(text = stringResource(R.string.sign_up_select_profile_image_btn), color = Color(0xFFBFBFBF))
                }
            }
        } else {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
        }

    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.sign_up_select_profile_image_description),
        color = Color(0xFFA9A9A9),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(
                Alignment.Center,
            ),
    )
    Spacer(modifier = Modifier.height(20.dp))
}

fun showUserIdDuplicationCheckResult(userIdDuplicated: Boolean?, context: Context) {
    if (userIdDuplicated == true) {
        Toast.makeText(
            context,
            context.getString(R.string.sign_up_check_user_id_duplication_success),
            Toast.LENGTH_SHORT
        ).show()
    } else if (userIdDuplicated == false) {
        Toast.makeText(context, context.getString(R.string.sign_up_check_user_id_duplication_fail), Toast.LENGTH_SHORT)
            .show()
    }
}

@Composable
@Preview(showBackground = true)
fun IdFieldPreview() {
    FamilyMomentsTheme {
        IdField(
            userIdFormatValidated = true,
            userIdDuplicated = false,
            checkIdFormat = {},
            checkIdDuplication = {},
            resetUserIdDuplicatedPass = {},
            onValueChange = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun NicknameFieldPreview() {
    FamilyMomentsTheme {
        NicknameField(
            nicknameFormatValidated = true,
            checkNicknameFormat = {},
            onTextFieldChange = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProfileImageFieldPreview() {
    FamilyMomentsTheme {
        Column {
            ProfileImageField(defaultBitmap, LocalContext.current) {}
        }
    }
}

@Composable
fun TermsField(onAllEssentialTermsAgree: (Boolean) -> Unit = {}) {
    val terms = remember {
        mutableStateListOf(
            SignUpTermUiState(true, R.string.sign_up_service_term_agree, CheckedStatus.UNCHECKED, SERVICE_TERM_URL),
            SignUpTermUiState(true, R.string.sign_up_identification_term_agree, CheckedStatus.UNCHECKED, PRIVACY_POLICY_URL)
        )
    }

    Column {
        TermItem(
            imageResources = listOf(R.drawable.circle_uncheck, R.drawable.circle_check),
            description = R.string.sign_up_all_term_agree,
            checked = if (terms.all { it.checkedStatus == CheckedStatus.CHECKED }) CheckedStatus.CHECKED else CheckedStatus.UNCHECKED,
            fontSize = 16,
            onCheckedChange = {
                for (index in terms.indices) {
                    terms[index] = terms[index].copy(checkedStatus = it)
                }
            })
        HorizontalDivider(modifier = Modifier.padding(vertical = 11.dp), thickness = 1.dp, color = AppColors.grey2)
        onAllEssentialTermsAgree(terms.filter { it.isEssential }.all { it.checkedStatus == CheckedStatus.CHECKED })

        val activity = LocalContext.current as Activity

        for (term in terms) {
            TermItem(
                imageResources = listOf(R.drawable.uncheck, R.drawable.check),
                description = term.description,
                checked = term.checkedStatus,
                fontSize = 13,
                onCheckedChange = {
                    terms[terms.indexOf(term)] = term.copy(checkedStatus = it)
                },
                onShowDetail = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(term.url))
                    activity.startActivity(intent)
                }
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

