package io.familymoments.app.feature.creatingfamily.screen

import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.choosingfamily.ChoosingFamilyHeaderButtonLayout

@Composable
fun CopyInvitationLinkScreen(inviteLink: String, navigate: () -> Unit = {}) {

    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = ClipData.newPlainText("inviteLink", inviteLink)

    ChoosingFamilyHeaderButtonLayout(
        headerBottomPadding = 16.dp,
        header = stringResource(R.string.family_invitation_link_header),
        button = stringResource(R.string.next_btn),
        onClick = navigate
    ) {
        Column {
            LinkTextField(inviteLink)
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp),
                onClick = {
                    clipboardManager.setPrimaryClip(clip)
                    Toast.makeText(
                        context,
                        context.getString(R.string.copy_invitation_link_message),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                shape = RoundedCornerShape(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.purple2),
                contentPadding = PaddingValues(vertical = 8.8.dp)
            ) {
                Text(text = stringResource(R.string.copy_invitation_link_btn))
            }
        }


    }
}

@Composable
private fun LinkTextField(inviteLink: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.5.dp, color = AppColors.grey2, shape = RoundedCornerShape(8.dp))
            .background(AppColors.pink5, shape = RoundedCornerShape(7.dp))
            .padding(vertical = 12.dp, horizontal = 11.dp),
    ) {
        BasicTextField(
            value = inviteLink,
            onValueChange = { },
            readOnly = true,
            textStyle = AppTypography.SH2_18.copy(AppColors.grey2),
            singleLine = true
        ) { innerTextField ->
            innerTextField()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCopyInvitationLinkScreen() {
    CopyInvitationLinkScreen("")
}
