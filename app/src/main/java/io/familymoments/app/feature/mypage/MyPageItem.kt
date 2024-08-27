package io.familymoments.app.feature.mypage

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.familymoments.app.R
import io.familymoments.app.core.util.PRIVACY_POLICY_URL
import io.familymoments.app.core.util.SERVICE_TERM_URL
import io.familymoments.app.feature.mypage.graph.MyPageRoute
import io.familymoments.app.feature.profile.graph.ProfileScreenRoute

sealed class MyPageItem(
    val route: String,
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int
) {
    data object Profile :
        MyPageItem(ProfileScreenRoute.View.name, R.string.my_page_label_profile, R.drawable.ic_my_page_profile)

    data object Password :
        MyPageItem(MyPageRoute.Password.name, R.string.my_page_label_password, R.drawable.ic_my_page_password)

    data object Notification :
        MyPageItem(
            MyPageRoute.Notification.name,
            R.string.my_page_label_notification,
            R.drawable.ic_my_page_notification
        )

    data object FamilyInvitationList :
        MyPageItem(
            MyPageRoute.FamilyInvitationList.name,
            R.string.my_page_label_family_invitation_list,
            R.drawable.ic_my_page_family_invitation_list
        )

    data object FamilySettings :
        MyPageItem(
            MyPageRoute.FamilySettings.name,
            R.string.my_page_label_family_settings,
            R.drawable.ic_my_page_family_settings
        )

    data object ServiceTerms :
        MyPageItem(
            SERVICE_TERM_URL,
            R.string.service_terms,
            R.drawable.ic_my_page_profile
        )

    data object PrivacyPolicy :
        MyPageItem(
            PRIVACY_POLICY_URL,
            R.string.privacy_policy,
            R.drawable.ic_my_page_profile
        )

    data object Logout :
        MyPageItem(MyPageRoute.Logout.name, R.string.my_page_label_logout, R.drawable.ic_my_page_logout)

    data object AccountDeletion :
        MyPageItem(
            MyPageRoute.AccountDeletion.name,
            R.string.my_page_label_account_deletion,
            R.drawable.ic_my_page_account_deletion
        )
}
