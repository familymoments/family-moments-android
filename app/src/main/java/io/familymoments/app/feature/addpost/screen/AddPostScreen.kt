package io.familymoments.app.feature.addpost.screen

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.core.util.FileUtil
import io.familymoments.app.core.util.keyboardAsState
import io.familymoments.app.feature.addpost.model.AddPostMode.*
import io.familymoments.app.feature.addpost.viewmodel.AddPostViewModel
import kotlinx.coroutines.launch

@Composable
fun AddPostScreen(
    modifier: Modifier,
    viewModel: AddPostViewModel,
    popBackStack: () -> Unit
) {
    val addPostUiState = viewModel.uiState.collectAsStateWithLifecycle().value
    if (addPostUiState.isSuccess == true) {
        popBackStack()
    }
    var content by remember { mutableStateOf(addPostUiState.existPostUiState.editContent) }
    val context = LocalContext.current
    val isKeyboardOpen by keyboardAsState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val modeEnum = addPostUiState.mode

    val uriList = remember {
        mutableStateListOf<Uri>(*addPostUiState.existPostUiState.editImages.map { Uri.parse(it) }.toTypedArray())
    }
    val launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        if (uriList.size < 10) {
            uriList.add(uri)
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            text =
            when (modeEnum) {
                ADD -> stringResource(id = R.string.add_post_title)
                EDIT -> stringResource(id = R.string.edit_post_title)
            },
            style = AppTypography.SH1_20,
            color = AppColors.deepPurple1,
            textAlign = TextAlign.Center
        )
        ImageRow(launcher, uriList)
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
            onValueChange = { content = it },
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
                        if (content.isNotEmpty() && uriList.isNotEmpty()) {
                            Modifier
                                .background(color = AppColors.deepPurple1)
                                .clickable {
                                    focusManager.clearFocus()
                                    scope.launch {
                                        val imageFiles = FileUtil.bitmapResize(context, uriList.toList())
                                        when (modeEnum) {
                                            ADD -> viewModel.addPost(content, imageFiles)
                                            EDIT -> {
                                                //todo 수정 로직으로 변경
                                            }
                                        }
                                    }
                                }
                        } else {
                            Modifier.background(color = AppColors.grey3)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text =
                    when (modeEnum) {
                        ADD -> stringResource(id = R.string.add_post_btn)
                        EDIT -> stringResource(R.string.edit_post_btn)
                    },
                    style = AppTypography.BTN4_18,
                    color = AppColors.grey6,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ImageRow(
    launcher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    imageUriList: MutableList<Uri>,
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .padding(vertical = 30.dp)
            .padding(start = 16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(63.dp)
                .border(width = 1.dp, color = AppColors.deepPurple3, shape = RoundedCornerShape(size = 6.dp))
                .clickable {
                    focusManager.clearFocus()
                    launcher.launch(
                        PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                .padding(start = 17.5.dp, top = 13.dp, end = 17.5.dp, bottom = 7.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_bottom_nav_album),
                    contentDescription = null,
                    tint = AppColors.grey2,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = AppTypography.BTN6_13.toSpanStyle()
                                .copy(color = if (imageUriList.isNotEmpty()) AppColors.purple2 else AppColors.grey2)
                        ) {
                            append(imageUriList.size.toString())
                        }
                        withStyle(style = AppTypography.BTN6_13.toSpanStyle().copy(color = AppColors.grey2)) {
                            append("/10")
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.width(9.dp))
        if (imageUriList.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                contentPadding = PaddingValues(end = 16.dp)
            ) {
                items(imageUriList.size) { i ->
                    Box {
                        AsyncImage(
                            modifier = Modifier
                                .size(63.dp)
                                .clip(shape = RoundedCornerShape(6.dp)),
                            model = imageUriList[i],
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            modifier = Modifier
                                .size(18.dp)
                                .offset(
                                    x = (63 - 13).dp,
                                    y = (-8).dp
                                )
                                .clickable {
                                    imageUriList.removeAt(i)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                text = stringResource(id = R.string.add_post_title),
                style = AppTypography.SH1_20,
                color = AppColors.deepPurple1,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 30.dp)
                    .padding(start = 16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(63.dp)
                        .border(width = 1.dp, color = AppColors.deepPurple3, shape = RoundedCornerShape(size = 6.dp))
                        .clickable {}
                        .padding(start = 17.5.dp, top = 13.dp, end = 17.5.dp, bottom = 7.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_bottom_nav_album),
                            contentDescription = null,
                            tint = AppColors.grey2,
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "0/10",
                            style = AppTypography.BTN6_13,
                            color = AppColors.grey2
                        )
                    }
                }
                Spacer(modifier = Modifier.width(9.dp))
            }
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
                value = "",
                onValueChange = { },
                textStyle = AppTypography.LB1_13.copy(color = AppColors.black1),
                decorationBox = { innerTextField ->
                    Text(
                        text = stringResource(id = R.string.add_post_write_content_hint),
                        style = AppTypography.LB1_13,
                        color = AppColors.grey3
                    )
                    innerTextField()
                },
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .fillMaxWidth()
                    .heightIn(min = 59.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .background(color = AppColors.deepPurple1)
                    .clickable {},
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.add_post_btn),
                    style = AppTypography.BTN4_18,
                    color = AppColors.grey6,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
