package io.familymoments.app.core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography

@Composable
fun SignUpTextFieldArea(
    modifier: Modifier = Modifier,
    title: String = "",
    hint: String,
    onValueChange: (TextFieldValue) -> Unit = {},
    showCheckButton: Boolean = false,
    checkButtonAvailable: Boolean = false,
    onCheckButtonClick: (TextFieldValue) -> Unit = {},
    showDeleteButton: Boolean = false,
    validated: Boolean = true,
    showWarningText: Boolean = false,
    warningText: String = "",
    isFocused: Boolean,
    showText: Boolean = true,
    enabled: Boolean = true,
    initialValue: String = "",
    checkButtonLabel: String = stringResource(id = R.string.sign_up_check_duplication_btn),
    warningTextAreaHeight: Int = 20,
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(initialValue))
    }
    var textFieldBorderColor by remember {
        mutableStateOf(AppColors.grey2)
    }
    var textColor by remember {
        mutableStateOf(AppColors.black1)
    }

    if (!isFocused) {
        textFieldBorderColor = AppColors.grey2
    }
    if ((isFocused && (validated || textFieldValue.text.isEmpty()))) {
        textFieldBorderColor = AppColors.purple2
    }
    if (textFieldValue.text.isNotEmpty() && !validated) {
        textFieldBorderColor = AppColors.red1
        textColor = if (showText) AppColors.red1 else AppColors.black1
    }
    Column {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                color = AppColors.grey8,
                style = AppTypography.SH3_16
            )
            Spacer(modifier = Modifier.height(7.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FMTextField(
                modifier = modifier.then(
                    if (showCheckButton) Modifier
                        .weight(1f)
                    else Modifier.fillMaxWidth()
                ),
                onValueChange = {
                    textFieldValue = it
                    onValueChange(textFieldValue)
                },
                value = textFieldValue,
                hint = hint,
                showDeleteButton = showDeleteButton,
                borderColor = textFieldBorderColor,
                textColor = textColor,
                showText = showText,
                enabled = enabled
            )
            if (showCheckButton) {
                CheckButton(checkButtonAvailable, textFieldValue, onCheckButtonClick, checkButtonLabel)
            }

        }

        if (showWarningText) {
            ShowWarningText(
                text = warningText,
                textFieldValue = textFieldValue,
                validation = validated,
                warningTextAreaHeight = warningTextAreaHeight,
                colorChange = {
                    textColor = it
                    textFieldBorderColor = it
                })
        }
    }
}

@Composable
private fun CheckButton(
    checkButtonAvailable: Boolean,
    textFieldValue: TextFieldValue,
    onCheckButtonClick: (TextFieldValue) -> Unit,
    label: String
) {
    ElevatedButton(
        modifier = Modifier
            .wrapContentWidth()
            .padding(start = 7.dp),
        enabled = textFieldValue.text.isNotEmpty() && checkButtonAvailable,
        onClick = { onCheckButtonClick(textFieldValue) },
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.grey8,
            contentColor = Color.White,
        ),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(13.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp
        )
    }
}


@Composable
private fun ShowWarningText(
    text: String,
    textFieldValue: TextFieldValue,
    validation: Boolean,
    warningTextAreaHeight: Int,
    colorChange: (Color) -> Unit,
) {

    if (textFieldValue.text.isNotEmpty() && !validation) {
        colorChange(AppColors.red1)
        Text(
            modifier = Modifier
                .padding(top = 3.dp, bottom = 4.dp),
            text = text,
            color = AppColors.red2,
            style = AppTypography.LB2_11
        )
    }
    if (textFieldValue.text.isEmpty() || validation) {
        colorChange(AppColors.black1)
        Spacer(modifier = Modifier.height(warningTextAreaHeight.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpTextFieldPreview() {
    SignUpTextFieldArea(title = "이메일", hint = "", showCheckButton = true, isFocused = false, onValueChange = {})
}

@Preview(showBackground = true)
@Composable
private fun SignUpTextFieldPreviewDisabled() {
    SignUpTextFieldArea(
        title = "이메일",
        hint = "example@gmail.com",
        isFocused = false,
        enabled = false,
        onValueChange = {})
}

@Preview(showBackground = true)
@Composable
private fun SignUpTextFieldPreviewFocused() {
    SignUpTextFieldArea(title = "이메일", hint = "", isFocused = true, onValueChange = {})
}

@Preview(showBackground = true)
@Composable
private fun ShowWarningTextPreview() {
    ShowWarningText(
        text = "이메일 형식이 올바르지 않습니다.",
        textFieldValue = TextFieldValue("example"),
        validation = false,
        warningTextAreaHeight = 20,
        colorChange = {})
}
