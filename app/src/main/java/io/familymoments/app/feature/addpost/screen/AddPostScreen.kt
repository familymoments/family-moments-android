package io.familymoments.app.feature.addpost.screen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.component.LoadingIndicator
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.core.util.POST_PHOTO_MAX_SIZE
import io.familymoments.app.core.util.keyboardAsState
import io.familymoments.app.core.util.oneClick
import io.familymoments.app.feature.addpost.AddPostMode
import io.familymoments.app.feature.addpost.AddPostMode.ADD
import io.familymoments.app.feature.addpost.AddPostMode.EDIT
import io.familymoments.app.feature.addpost.uistate.AddPostUiState
import io.familymoments.app.feature.addpost.viewmodel.AddPostViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddPostScreen(
    modifier: Modifier, viewModel: AddPostViewModel, popBackStack: () -> Unit
) {
    val addPostUiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var content by remember { mutableStateOf(addPostUiState.existPostUiState.editContent) }
    val context = LocalContext.current

    LaunchedEffectWithSuccess(addPostUiState, popBackStack, context, viewModel)

    val uriList = viewModel.uriState
    val launcher = generateVisualMediaRequestLauncher { uris ->
        viewModel.addImages(
            uris = uris,
            context = context,
            errorMessage = context.getString(R.string.add_post_exceed_max_photo_size_error, POST_PHOTO_MAX_SIZE)
        )
    }

    val scope = rememberCoroutineScope()
    val isKeyboardOpen by keyboardAsState()
    val focusManager = LocalFocusManager.current
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    AddPostScreenUI(
        modifier = modifier,
        modeEnum = addPostUiState.mode,
        focusManager = focusManager,
        uriList = uriList,
        onLaunchPickVisualMediaRequest = {
            launcher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onRemoveImageClicked = {
          viewModel.removeImage(it)
        },
        content = content,
        onContentUpdate = { content = it },
        isKeyboardOpen = isKeyboardOpen,
        isLoading = isLoading,
        onButtonClick = {
            onUploadClicked(focusManager, scope, addPostUiState, viewModel, content)
        })
}

private fun onUploadClicked(
    focusManager: FocusManager,
    scope: CoroutineScope,
    addPostUiState: AddPostUiState,
    viewModel: AddPostViewModel,
    content: String
) {
    focusManager.clearFocus()
    scope.launch {
        when (addPostUiState.mode) {
            ADD -> viewModel.addPost(content)
            EDIT -> viewModel.editPost(
                addPostUiState.existPostUiState.editPostId, content
            )
        }
    }
}

@Composable
private fun LaunchedEffectWithSuccess(
    addPostUiState: AddPostUiState, popBackStack: () -> Unit, context: Context, viewModel: AddPostViewModel
) {
    LaunchedEffect(addPostUiState.isSuccess) {
        if (addPostUiState.isSuccess == true) {
            popBackStack()
        } else if (addPostUiState.isSuccess == false) {
            Toast.makeText(context, addPostUiState.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.initSuccessState()
        }
    }
}


@Composable
private fun generateVisualMediaRequestLauncher(
    addImages: (List<Uri>) -> Unit
) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(POST_PHOTO_MAX_SIZE)
    ) { uris ->
        if (uris.isNotEmpty<@JvmSuppressWildcards Uri>()) {
            addImages(uris)
        }
    }

@Composable
private fun AddPostScreenUI(
    modifier: Modifier = Modifier,
    focusManager: FocusManager = LocalFocusManager.current,
    modeEnum: AddPostMode = ADD,
    uriList: SnapshotStateList<Uri> = mutableStateListOf(),
    onLaunchPickVisualMediaRequest: () -> Unit = {},
    content: String = "",
    onContentUpdate: (String) -> Unit = {},
    isKeyboardOpen: Boolean = false,
    isLoading: Boolean = false,
    onRemoveImageClicked: (Int) -> Unit = {},
    onButtonClick: () -> Unit = {},
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp), text = when (modeEnum) {
                    ADD -> stringResource(id = R.string.add_post_title)
                    EDIT -> stringResource(id = R.string.edit_post_title)
                }, style = AppTypography.SH1_20, color = AppColors.grey8, textAlign = TextAlign.Center
            )
            ImageRow(focusManager, uriList, onRemoveImageClicked, onLaunchPickVisualMediaRequest)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                text = stringResource(id = R.string.add_post_write_content),
                style = AppTypography.B1_16,
                color = AppColors.black1,
                textAlign = TextAlign.Center
            )
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .heightIn(min = 268.dp)
                    .clip(shape = RoundedCornerShape(7.dp))
                    .background(color = AppColors.grey5)
                    .padding(all = 20.dp),
                value = content,
                onValueChange = { onContentUpdate(it) },
                textStyle = AppTypography.LB1_13.copy(color = AppColors.black1),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.add_post_write_content_hint),
                            style = AppTypography.LB1_13,
                            color = AppColors.grey3
                        )
                    }
                    innerTextField()
                },
            )
            if (!isKeyboardOpen) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .fillMaxWidth()
                        .heightIn(min = 59.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .then(
                            if (content
                                    .trim()
                                    .isNotEmpty() && uriList.isNotEmpty()
                            ) {
                                Modifier
                                    .background(color = AppColors.grey8)
                                    .oneClick(onButtonClick)
                            } else {
                                Modifier.background(color = AppColors.grey3)
                            }
                        ), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (modeEnum) {
                            ADD -> stringResource(id = R.string.add_post_btn)
                            EDIT -> stringResource(R.string.edit_post_btn)
                        }, style = AppTypography.BTN4_18, color = AppColors.grey6, textAlign = TextAlign.Center
                    )
                }
            }
        }
        LoadingIndicator(isLoading = isLoading)
    }
}

@Composable
private fun ImageRow(
    focusManager: FocusManager = LocalFocusManager.current,
    imageList: SnapshotStateList<Uri> = mutableStateListOf(),
    onRemoveImageClicked: (Int) -> Unit = {},
    onPickVisualMediaRequest: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(vertical = 30.dp)
            .padding(start = 16.dp),
    ) {
        Box(modifier = Modifier
            .width(63.dp)
            .heightIn(min = 63.dp)
            .border(width = 1.dp, color = AppColors.grey7, shape = RoundedCornerShape(size = 6.dp))
            .oneClick(1000) {
                focusManager.clearFocus()
                onPickVisualMediaRequest()
            }
            .padding(top = 13.dp, bottom = 7.dp)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_bottom_nav_album),
                    contentDescription = null,
                    tint = AppColors.grey2,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = buildAnnotatedString {
                    withStyle(
                        style = AppTypography.BTN6_13.toSpanStyle()
                            .copy(color = if (imageList.isNotEmpty()) AppColors.purple2 else AppColors.grey2)
                    ) {
                        append(imageList.size.toString())
                    }
                    withStyle(style = AppTypography.BTN6_13.toSpanStyle().copy(color = AppColors.grey2)) {
                        append("/$POST_PHOTO_MAX_SIZE")
                    }
                })
            }
        }
        Spacer(modifier = Modifier.width(9.dp))
        if (imageList.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(9.dp), contentPadding = PaddingValues(end = 16.dp)
            ) {
                items(imageList.size) { i ->
                    Box {
                        AsyncImage(
                            modifier = Modifier
                                .size(63.dp)
                                .clip(shape = RoundedCornerShape(6.dp)),
                            model = imageList[i],
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            modifier = Modifier
                                .size(18.dp)
                                .offset(
                                    x = (63 - 13).dp, y = (-8).dp
                                )
                                .oneClick(1000) {
                                    onRemoveImageClicked(i)
                                },
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_text_field_clear),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddPostScreenPreview() {
    FamilyMomentsTheme {
        AddPostScreenUI(content = "test")
    }
}
