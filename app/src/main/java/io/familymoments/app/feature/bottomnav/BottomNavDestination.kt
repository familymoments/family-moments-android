package io.familymoments.app.feature.bottomnav

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
