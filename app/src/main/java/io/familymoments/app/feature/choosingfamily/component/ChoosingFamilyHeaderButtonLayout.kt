package io.familymoments.app.feature.choosingfamily.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography

@Composable
fun ChoosingFamilyHeaderButtonLayout(
    headerBottomPadding: Dp,
    header: String = "",
    button: String = "",
    buttonEnabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Header(headerBottomPadding, header)
        Box(modifier = Modifier.weight(1f)) {
            content()

        }
        NextButton(button, onClick, buttonEnabled)
    }

}

@Composable
private fun Header(headerBottomPadding: Dp, header: String) {
    Text(
        modifier = Modifier.padding(top = 55.dp, bottom = headerBottomPadding),
        text = header,
        style = AppTypography.SH2_18,
        color = AppColors.grey8
    )
}

@Composable
private fun NextButton(button: String, onClick: () -> Unit, buttonEnabled: Boolean) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.grey8,
            disabledContainerColor = AppColors.grey3
        ),
        shape = RoundedCornerShape(60.dp),
        contentPadding = PaddingValues(vertical = 18.dp),
        onClick = onClick,
        enabled = buttonEnabled
    ) {
        Text(text = button, style = AppTypography.BTN4_18, color = AppColors.grey6)
    }
}
