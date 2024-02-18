package io.familymoments.app.feature.bottomnav.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.familymoments.app.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val iconResId: Int,
    @StringRes val labelResId: Int
) {
    data object Home :
        BottomNavItem(BottomNavRoute.HOME.name, R.drawable.ic_bottom_nav_home, R.string.bottom_nav_label_home)

    data object Album :
        BottomNavItem(BottomNavRoute.ALBUM.name, R.drawable.ic_bottom_nav_album, R.string.bottom_nav_label_album)

    data object AddPost :
        BottomNavItem(BottomNavRoute.ADD_POST.name, R.drawable.ic_bottom_nav_add_post, R.string.bottom_nav_label_home)

    data object Calendar :
        BottomNavItem(BottomNavRoute.CALENDAR.name, R.drawable.ic_bottom_nav_calendar, R.string.bottom_nav_label_calendar)

    data object MyPage :
        BottomNavItem(BottomNavRoute.MY_PAGE.name, R.drawable.ic_bottom_nav_my_page, R.string.bottom_nav_label_my_page)
}


enum class BottomNavRoute {
    HOME, ALBUM, ADD_POST, CALENDAR, MY_PAGE
}
