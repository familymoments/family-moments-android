package io.familymoments.app.feature.forgotid.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.R
import io.familymoments.app.core.component.FMButton
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography

@Composable
fun FindIdScreen(
    userId: String,
    goToLogin:()->Unit,
    goToForgotPwd:()->Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(83.dp))
        Text(
            text = stringResource(id = R.string.login_forgot_id),
            style = AppTypography.H1_36,
            color = AppColors.grey8
        )
        Spacer(modifier = Modifier.height(31.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.forgot_id_display_id_label),
            style = AppTypography.B1_16,
            color = AppColors.grey8,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(31.dp))
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .width(240.dp)
                    .border(width = 1.5.dp, color = AppColors.grey2, shape = RoundedCornerShape(8.dp))
                    .background(AppColors.pink5, shape = RoundedCornerShape(7.dp))
                    .padding(vertical = 12.dp, horizontal = 11.dp),
            ) {
                BasicTextField(
                    value = TextFieldValue(userId),
                    onValueChange = { },
                    readOnly = true,
                    textStyle = AppTypography.SH2_18.copy(AppColors.grey8),
                    singleLine = true
                ) { innerTextField ->
                    innerTextField()
                }
            }
        }
        Spacer(modifier = Modifier.height(270.dp))
        Row {
            FMButton(
                onClick = goToLogin,
                text = stringResource(R.string.forgot_id_go_to_login_btn),
                modifier = Modifier.weight(1f),
                containerColor = AppColors.purple2,
                contentPaddingValues = PaddingValues(vertical = 17.dp, horizontal = 18.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            FMButton(
                onClick = goToForgotPwd,
                text = stringResource(R.string.forgot_id_go_to_forgot_pwd_btn),
                modifier = Modifier.weight(1f),
                contentPaddingValues = PaddingValues(vertical = 17.dp, horizontal = 18.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FindIdScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        FindIdScreen("test1111",{}){}
    }
}
