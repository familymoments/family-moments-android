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
        BottomNavItem(BottomNavDestination.Home.route, R.drawable.ic_bottom_nav_home, R.string.bottom_nav_label_home)

    data object Album :
        BottomNavItem(BottomNavDestination.Album.route, R.drawable.ic_bottom_nav_album, R.string.bottom_nav_label_album)

    data object AddPost :
        BottomNavItem(BottomNavDestination.AddPost.route, R.drawable.ic_bottom_nav_add_post, R.string.bottom_nav_label_home)

    data object Calendar :
        BottomNavItem(BottomNavDestination.Calendar.route, R.drawable.ic_bottom_nav_calendar, R.string.bottom_nav_label_calendar)

    data object MyPage :
        BottomNavItem(BottomNavDestination.MyPage.route, R.drawable.ic_bottom_nav_my_page, R.string.bottom_nav_label_my_page)
}

sealed interface BottomNavDestination {
    val route: String

    data object Home : BottomNavDestination {
        override val route: String = "Home"
    }

    data object Album : BottomNavDestination {
        override val route: String = "Album"
    }

    data object AddPost : BottomNavDestination {
        override val route: String = "AddPost"
    }

    data object Calendar : BottomNavDestination {
        override val route: String = "Calendar"
    }

    data object MyPage : BottomNavDestination {
        override val route: String = "MyPage"
    }
}
