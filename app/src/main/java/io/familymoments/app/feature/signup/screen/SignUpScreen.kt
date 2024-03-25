package io.familymoments.app.feature.signup.screen

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.AppBarScreen
import io.familymoments.app.core.component.CheckedStatus
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.component.FMCheckBox
import io.familymoments.app.core.component.SignUpTextFieldArea
import io.familymoments.app.core.network.api.SignInService
import io.familymoments.app.core.network.repository.impl.SignInRepositoryImpl
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.core.util.FileUtil.convertUriToBitmap
import io.familymoments.app.feature.signup.model.request.CheckEmailRequest
import io.familymoments.app.feature.signup.model.request.CheckIdRequest
import io.familymoments.app.feature.signup.model.request.SignUpRequest
import io.familymoments.app.feature.signup.model.response.CheckEmailResponse
import io.familymoments.app.feature.signup.model.response.CheckIdResponse
import io.familymoments.app.feature.signup.model.response.SignUpResponse
import io.familymoments.app.feature.signup.model.uistate.SignUpInfoUiState
import io.familymoments.app.feature.signup.model.uistate.SignUpTermUiState
import io.familymoments.app.feature.signup.viewmodel.SignUpViewModel
import okhttp3.MultipartBody

@Composable
fun SignUpScreen(viewModel: SignUpViewModel) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val userIdDuplicated = uiState.value.signUpValidatedUiState.userIdDuplicated
    val emailDuplicated = uiState.value.signUpValidatedUiState.emailDuplicated
    val defaultProfileImageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_profile)

    var password: String by remember { mutableStateOf("") }
    var allEssentialTermsAgree by remember {
        mutableStateOf(false)
    }

    var passwordSameCheck by remember {
        mutableStateOf(false)
    }
    var signUpInfoUiState: SignUpInfoUiState by remember {
        mutableStateOf(
            SignUpInfoUiState(bitmap = defaultProfileImageBitmap)
        )
    }

    LaunchedEffect(userIdDuplicated) {
        showUserIdDuplicationCheckResult(userIdDuplicated, context)
    }
    LaunchedEffect(emailDuplicated) {
        showEmailDuplicationCheckResult(emailDuplicated, context)
    }

    AppBarScreen(
        title = {
            Text(
                text = stringResource(id = R.string.sign_up_activity_app_bar_title),
                style = AppTypography.SH3_16,
                color = AppColors.deepPurple1
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier.clickable {
                    (context as Activity).finish()
                },
                painter = painterResource(id = R.drawable.ic_app_bar_back),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(43.dp))
            IdField(
                userIdFormatValidated = uiState.value.signUpValidatedUiState.userIdValidated,
                checkIdFormat = viewModel::checkIdFormat,
                checkIdDuplication = viewModel::checkIdDuplication
            ) {
                signUpInfoUiState = signUpInfoUiState.copy(id = it)
            }
            FirstPasswordField(
                passwordFormatValidated = uiState.value.signUpValidatedUiState.passwordValidated,
                checkPasswordFormat = viewModel::checkPasswordFormat
            ) {
                password = it
                signUpInfoUiState = signUpInfoUiState.copy(password = it)
            }
            SecondPasswordField(
                firstPassword = signUpInfoUiState.password,
                checkPasswordIsSame = { passwordSameCheck = it },
            )
            NameField { signUpInfoUiState = signUpInfoUiState.copy(name = it) }
            EmailField(
                checkEmailFormat = viewModel::checkEmailFormat,
                checkEmailDuplication = viewModel::checkEmailDuplication,
                emailFormatValidated = uiState.value.signUpValidatedUiState.emailValidated,
            ) { signUpInfoUiState = signUpInfoUiState.copy(email = it) }
            BirthDayField { signUpInfoUiState = signUpInfoUiState.copy(birthDay = it) }
            NicknameField(
                nicknameFormatValidated = uiState.value.signUpValidatedUiState.nicknameValidated,
                checkNicknameFormat = viewModel::checkNicknameFormat,
            ) { signUpInfoUiState = signUpInfoUiState.copy(nickname = it) }
            ProfileImageField(defaultProfileImageBitmap) {
                signUpInfoUiState = signUpInfoUiState.copy(bitmap = it)
            }
            Spacer(modifier = Modifier.height(53.dp))
            TermsField { allEssentialTermsAgree = it }
            StartButtonField(
                viewModel::executeSignUp,
                signUpInfoUiState,
                userIdDuplicated ?: false,
                passwordSameCheck,
                emailDuplicated ?: false,
                allEssentialTermsAgree
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

private fun showUserIdDuplicationCheckResult(userIdDuplicated: Boolean?, context: Context) {
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

private fun showEmailDuplicationCheckResult(emailDuplicated: Boolean?, context: Context) {
    if (emailDuplicated == true) {
        Toast.makeText(
            context,
            context.getString(R.string.sign_up_check_email_duplication_success),
            Toast.LENGTH_SHORT
        ).show()
    } else if (emailDuplicated == false) {
        Toast.makeText(context, context.getString(R.string.sign_up_check_email_duplication_fail), Toast.LENGTH_SHORT)
            .show()
    }
}

@Composable
fun IdField(
    userIdFormatValidated: Boolean,
    checkIdFormat: (String) -> Unit,
    checkIdDuplication: (String) -> Unit,
    onValueChange: (String) -> Unit
) {
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
                onValueChange(it.text)
                checkIdFormat(it.text)
            },
            showCheckButton = true,
            checkButtonAvailable = userIdFormatValidated,
            onCheckButtonClick = {
                checkIdDuplication(it.text)
            },
            validated = userIdFormatValidated,
            showWarningText = true,
            warningText = stringResource(id = R.string.sign_up_id_validation_warning),
            isFocused = isFocused
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun FirstPasswordField(
    passwordFormatValidated: Boolean,
    checkPasswordFormat: (String) -> Unit,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    SignUpTextFieldArea(
        modifier = Modifier.onFocusChanged {
            isFocused = it.isFocused
        },
        title = stringResource(id = R.string.sign_up_password_field_title),
        hint = stringResource(R.string.sign_up_password_field_hint),
        onValueChange = {
            onValueChange(it.text)
            checkPasswordFormat(it.text)
        },
        showDeleteButton = true,
        showWarningText = true,
        warningText = stringResource(id = R.string.sign_up_password_validation_warning),
        validated = passwordFormatValidated,
        isFocused = isFocused
    )
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun SecondPasswordField(
    firstPassword: String,
    checkPasswordIsSame: (Boolean) -> Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    var isPasswordSame by remember {
        mutableStateOf(false)
    }
    Column {
        SignUpTextFieldArea(
            modifier = Modifier.onFocusChanged {
                isFocused = it.isFocused
            },
            title = stringResource(id = R.string.sign_up_password_check_field_title),
            hint = stringResource(R.string.sign_up_password_check_field_hint),
            onValueChange = {
                isPasswordSame = firstPassword == it.text
                checkPasswordIsSame(isPasswordSame)
            },
            showDeleteButton = true,
            showWarningText = true,
            warningText = stringResource(id = R.string.sign_up_password_check_validation_warning),
            validated = isPasswordSame,
            isFocused = isFocused
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun NameField(onValueChange: (String) -> Unit) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    SignUpTextFieldArea(
        modifier = Modifier.onFocusChanged {
            isFocused = it.isFocused
        },
        title = stringResource(id = R.string.sign_up_name_field_title),
        hint = stringResource(R.string.sign_up_name_field_hint),
        onValueChange = { onValueChange(it.text) },
        isFocused = isFocused
    )

    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun EmailField(
    checkEmailFormat: (String) -> Unit,
    checkEmailDuplication: (String) -> Unit,
    emailFormatValidated: Boolean,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    Column {
        SignUpTextFieldArea(
            modifier = Modifier.onFocusChanged {
                isFocused = it.isFocused
            },
            title = stringResource(R.string.sign_up_email_field_title),
            hint = stringResource(id = R.string.sign_up_email_field_hint),
            onValueChange = {
                onValueChange(it.text)
                checkEmailFormat(it.text)
            },
            showCheckButton = true,
            checkButtonAvailable = emailFormatValidated,
            onCheckButtonClick = {
                checkEmailDuplication(it.text)
            },
            isFocused = isFocused,
            showWarningText = true,
            warningText = stringResource(id = R.string.sign_up_password_check_validation_warning),
            validated = emailFormatValidated
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun BirthDayField(onTextFieldChange: (String) -> Unit) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    SignUpTextFieldArea(
        modifier = Modifier.onFocusChanged {
            isFocused = it.isFocused
        },
        title = stringResource(id = R.string.sign_up_birthday_field_title),
        hint = stringResource(R.string.sign_up_birthday_field_hint),
        onValueChange = { onTextFieldChange(it.text) },
        isFocused = isFocused
    )

    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun NicknameField(
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
    Column {
        SignUpTextFieldArea(
            modifier = Modifier.onFocusChanged {
                isFocused = it.isFocused
            },
            title = stringResource(id = R.string.sign_up_nickname_field_title),
            hint = stringResource(R.string.sign_up_nickname_field_hint),
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
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun ProfileImageSelectDropDownMenu(
    isMenuExpanded: Boolean,
    isMenuExpandedChanged: (Boolean) -> Unit,
    defaultProfileImageBitmap: Bitmap,
    getBitmap: (Bitmap?) -> Unit,
) {
    val context = LocalContext.current
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    //갤러리에서 사진 선택 후 bitmap 으로 변환
    val launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it == null) return@rememberLauncherForActivityResult
        bitmap = convertUriToBitmap(it, context)
        getBitmap(bitmap)
    }

    DropdownMenu(expanded = isMenuExpanded,
        onDismissRequest = { isMenuExpandedChanged(false) }) {
        MenuItemGallerySelect(launcher, isMenuExpandedChanged)
        MenuItemDefaultImage({ getBitmap(defaultProfileImageBitmap) }, isMenuExpandedChanged)
    }
}

@Composable
fun MenuItemGallerySelect(
    launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    isMenuExpandedChanged: (Boolean) -> Unit
) {
    DropdownMenuItem(onClick = {
        launcher.launch(
            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
        isMenuExpandedChanged(false)
    }) {
        Text(text = stringResource(R.string.sign_up_select_profile_image_drop_down_menu_gallery))
    }
}

@Composable
fun MenuItemDefaultImage(getDefaultProfileImageBitmap: () -> Unit, isMenuExpandedChanged: (Boolean) -> Unit) {
    DropdownMenuItem(onClick = {
        getDefaultProfileImageBitmap()
        isMenuExpandedChanged(false)
    }) {
        Text(text = stringResource(R.string.sign_up_select_profile_image_drop_down_menu_default_image))
    }
}

@Composable
fun ProfileImageField(defaultProfileImageBitmap: Bitmap, onBitmapChange: (Bitmap) -> Unit) {
    var isMenuExpanded: Boolean by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
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
            backgroundColor = Color(0xFFF3F4F7),
        ),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
    ) {
        ProfileImageSelectDropDownMenu(isMenuExpanded, { isMenuExpanded = it }, defaultProfileImageBitmap) {
            bitmap = it
        }
        bitmap?.let {
            onBitmapChange(it)
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(400.dp)
            )
        }

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

@Composable
fun StartButtonField(
    onClick: (SignUpInfoUiState) -> Unit,
    signUpInfoUiState: SignUpInfoUiState,
    idDuplicationCheck: Boolean,
    passwordSameCheck: Boolean,
    emailDuplicationCheck: Boolean,
    allEssentialTermsAgree: Boolean
) {
    var signUpEnable by remember {
        mutableStateOf(false)
    }
    signUpEnable = idDuplicationCheck && passwordSameCheck && emailDuplicationCheck && allEssentialTermsAgree
    FMButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(signUpInfoUiState) },
        enabled = signUpEnable,
        text = stringResource(R.string.sign_up_btn)
    )
}

@Composable
fun TermItem(
    imageResources: List<Int>,
    description: Int,
    checked: CheckedStatus,
    fontSize: Int,
    onCheckedChange: (CheckedStatus) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FMCheckBox(
            imageResources = imageResources,
            defaultStatus = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            modifier = Modifier.padding(start = 14.dp),
            text = stringResource(id = description),
            fontSize = fontSize.sp
        )
    }
}

@Composable
fun TermsList(
    list: List<SignUpTermUiState>,
    onTermCheckedChange: (Int, CheckedStatus) -> Unit,
    onTermsCheckedChange: (Boolean) -> Unit
) {
    onTermsCheckedChange(list.filter { it.isEssential }.all { it.checkedStatus == CheckedStatus.CHECKED })
    for (index in list.indices) {
        TermItem(
            imageResources = listOf(R.drawable.uncheck, R.drawable.check),
            description = list[index].description,
            checked = list[index].checkedStatus,
            fontSize = 13,
            onCheckedChange = { onTermCheckedChange(index, it) })
    }
}

@Composable
fun TermsField(onAllEssentialTermsAgree: (Boolean) -> Unit) {

    val terms = remember {
        mutableStateListOf(
            SignUpTermUiState(true, R.string.sign_up_service_term_agree, CheckedStatus.UNCHECKED),
            SignUpTermUiState(true, R.string.sign_up_identification_term_agree, CheckedStatus.UNCHECKED),
            SignUpTermUiState(false, R.string.sign_up_marketing_alarm_term_agree, CheckedStatus.UNCHECKED)
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
        Divider(modifier = Modifier.padding(vertical = 11.dp), thickness = 1.dp, color = AppColors.grey2)
        TermsList(list = terms, { index, checkedStatus ->
            terms[index] = terms[index].copy(checkedStatus = checkedStatus)
        }) { onAllEssentialTermsAgree(it) }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    FamilyMomentsTheme {
        SignUpScreen(
            SignUpViewModel(
                SignInRepositoryImpl(object : SignInService {
                    override suspend fun checkId(checkIdRequest: CheckIdRequest): CheckIdResponse {
                        return CheckIdResponse(true, 200, "", "")
                    }

                    override suspend fun checkEmail(checkEmailRequest: CheckEmailRequest): CheckEmailResponse {
                        return CheckEmailResponse(true, 200, "", "")
                    }

                    override suspend fun executeSignUp(
                        profileImg: MultipartBody.Part,
                        signUpRequest: SignUpRequest
                    ): SignUpResponse {
                        TODO("Not yet implemented")
                    }
                })
            )
        )
    }
}
