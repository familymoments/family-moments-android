package io.familymoments.app.feature.creatingfamily.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.LoadingIndicator
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.creatingfamily.model.UploadCycle
import io.familymoments.app.feature.choosingfamily.ChoosingFamilyHeaderButtonLayout
import io.familymoments.app.feature.creatingfamily.viewmodel.CreatingFamilyViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SetAlarmScreen(
    viewModel: CreatingFamilyViewModel,
    navigate: (String) -> Unit = {}
) {
    val familyInfo = viewModel.familyProfile.collectAsStateWithLifecycle().value
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var uploadCycleTextFieldValue by remember {
        mutableStateOf(TextFieldValue())
    }
    var uploadCycle by remember {
        mutableStateOf(UploadCycle.NONE)
    }
    val createFamilyResultUiState = viewModel.createFamilyResultUiState.collectAsStateWithLifecycle()
    if (createFamilyResultUiState.value.isSuccess == true) {
        navigate(createFamilyResultUiState.value.result.inviteCode)
    }
    LoadingIndicator(isLoading = createFamilyResultUiState.value.isLoading ?: false)
    ChoosingFamilyHeaderButtonLayout(
        headerBottomPadding = 34.dp,
        header = stringResource(id = R.string.select_create_family_header),
        button = stringResource(R.string.create_family_btn),
        onClick = {
            viewModel.createFamily(familyInfo.copy(uploadCycle = uploadCycle.number))
        }
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {
                isExpanded = !isExpanded
            }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isExpanded) {
                            Modifier.border(width = 1.5.dp, color = AppColors.purple2, shape = RoundedCornerShape(8.dp))
                        } else {
                            Modifier
                        }
                    )
                    .background(AppColors.grey6, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 12.dp, horizontal = 11.dp),
            ) {
                BasicTextField(
                    value = uploadCycleTextFieldValue,
                    onValueChange = { uploadCycleTextFieldValue = it },
                    textStyle = AppTypography.LB1_13.copy(AppColors.black1),
                    readOnly = true
                ) { innerTextField ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (uploadCycleTextFieldValue.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.alarm_cycle_text_field_hint),
                                style = AppTypography.LB1_13,
                                color = AppColors.grey2
                            )
                        }
                        innerTextField()
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 5.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            TextFieldExpandedIcon(isExpanded)
                        }
                    }
                }
            }
            ExposedDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }) {
                UploadCycle.entries.forEach {
                    DropdownMenuItem(onClick = {
                        isExpanded = !isExpanded
                        uploadCycleTextFieldValue = TextFieldValue(it.value)
                        uploadCycle = it
                    }) {
                        Text(text = it.value)
                    }
                }
            }

        }
    }
}

@Composable
private fun TextFieldExpandedIcon(isExpanded: Boolean) {
    if (isExpanded) {
        Icon(
            painter = painterResource(id = R.drawable.ic_drop_down_expanded_trailing),
            contentDescription = null,
            tint = Color.Unspecified
        )
    } else {
        Icon(
            painter = painterResource(id = R.drawable.ic_drop_down_expanded_trailing),
            contentDescription = null,
            tint = AppColors.grey2,
            modifier = Modifier.rotate(180f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSetAlarmScreen() {
    SetAlarmScreen( hiltViewModel()) {}
}
