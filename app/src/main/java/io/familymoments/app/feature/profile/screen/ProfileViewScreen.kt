package io.familymoments.app.feature.profile.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.network.model.UserProfile
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.profile.viewmodel.ProfileViewViewModel

@Composable
fun ProfileViewScreen(
    navigateToProfileEdit: (UserProfile) -> Unit,
    viewModel: ProfileViewViewModel
) {
    val profileViewUiState = viewModel.profileViewUiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.profile_title),
            style = AppTypography.B1_16,
            color = AppColors.black1,
            modifier = Modifier.padding(vertical = 18.dp)
        )
        UserProfileInfo(
            userProfile = profileViewUiState.value.userProfile,
            navigateToProfileEdit = navigateToProfileEdit
        )
        Divider(
            color = AppColors.grey3, thickness = 0.6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 29.dp, bottom = 13.dp)
        )
        InfoBoxes(
            totalUpload = profileViewUiState.value.userProfile.totalUpload,
            duration = profileViewUiState.value.userProfile.duration
        )
        Divider(
            color = AppColors.grey3, thickness = 0.6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 138.dp)
        )
    }
}

@Composable
private fun UserProfileInfo(
    userProfile: UserProfile,
    navigateToProfileEdit: (UserProfile) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.size(24.dp))
        Box(
            modifier = Modifier.padding(top = 4.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                model = userProfile.profileImg,
                contentDescription = "profile",
                contentScale = ContentScale.Crop,
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_profile_modify_button),
            contentDescription = "profile_modify_button",
            modifier = Modifier
                .size(24.dp)
                .clickable { navigateToProfileEdit(userProfile) }
        )
    }
    Text(
        text = userProfile.nickName,
        style = AppTypography.H2_24,
        color = AppColors.deepPurple1,
        modifier = Modifier.padding(bottom = 12.dp)
    )
    Text(
        text = userProfile.email,
        style = AppTypography.B1_16,
        color = AppColors.grey1
    )
}

@Composable
private fun InfoBoxes(
    totalUpload: Int = 0,
    duration: Int = 0
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        InfoBoxItem(
            modifier = Modifier.weight(1f),
            titleResId = R.string.profile_total_upload,
            value = totalUpload,
            colors = GradientColors.purpleGradient,
            textColor = AppColors.purple2
        )
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        InfoBoxItem(
            modifier = Modifier.weight(1f),
            titleResId = R.string.profile_app_usage_period,
            value = duration,
            colors = GradientColors.pinkGradient,
            textColor = AppColors.pink1
        )
    }
}

@Composable
fun InfoBoxItem(
    modifier: Modifier,
    @StringRes titleResId: Int,
    value: Int,
    colors: List<Color>,
    textColor: Color
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = Offset.Infinite,
                    end = Offset.Zero
                ),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = titleResId),
                style = AppTypography.BTN6_13,
                color = Color.White,
                modifier = Modifier.padding(start = 11.dp, top = 14.dp)
            )
            Spacer(modifier = Modifier.padding(vertical = 3.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 11.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Text(
                        text = value.toString(),
                        style = AppTypography.LB1_13,
                        color = textColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

private object GradientColors {
    val purpleGradient = listOf(AppColors.purple2, AppColors.purple2.copy(alpha = 0.56f))
    val pinkGradient = listOf(AppColors.pink1, AppColors.pink1.copy(alpha = 0.56f))
}


@Preview(showBackground = true)
@Composable
fun ProfileViewScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.profile_title),
            style = AppTypography.B1_16,
            color = AppColors.black1,
            modifier = Modifier.padding(vertical = 18.dp)
        )
        UserProfileInfo(
            userProfile = UserProfile(profileImg = "", name = "홍길동", nickName = "아부지", email = "familyMoments@gmail.com"),
            navigateToProfileEdit = { }
        )
        Divider(
            color = AppColors.grey3, thickness = 0.6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 29.dp, bottom = 13.dp)
        )
        InfoBoxes(
            totalUpload = 8,
            duration = 134
        )
        Divider(
            color = AppColors.grey3, thickness = 0.6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 138.dp)
        )
    }
}
