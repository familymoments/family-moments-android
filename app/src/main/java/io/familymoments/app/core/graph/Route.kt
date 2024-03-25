package io.familymoments.app.core.graph

import androidx.navigation.NavType
import androidx.navigation.navArgument
import io.familymoments.app.feature.profile.graph.ProfileScreenRoute
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed interface Route {
    val route: String

    data object CalendarDay : Route {
        override val route: String = "CalendarDay"
        const val localDateStringArgs = "localDateString"
        val routeWithArs = "$route?$localDateStringArgs={$localDateStringArgs}"
        val arguments = listOf(navArgument(localDateStringArgs) { type = NavType.StringType })

        fun getRoute(localDateString: String): String = "$route?$localDateStringArgs=$localDateString"
    }

    data object ProfileEdit : Route {
        override val route: String = ProfileScreenRoute.Edit.name
        const val nameArg = "name"
        const val nicknameArg = "nickname"
        const val birthdateArg = "birthdate"
        const val profileImgArg = "profileImg"
        val routeWithArgs = "$route/{$nameArg}/{$nicknameArg}/{$birthdateArg}/{$profileImgArg}"
        val arguments = listOf(
            navArgument(nameArg) { type = NavType.StringType },
            navArgument(nicknameArg) { type = NavType.StringType },
            navArgument(birthdateArg) { type = NavType.StringType },
            navArgument(profileImgArg) { type = NavType.StringType }
        )

        fun getRoute(name: String, nickname: String, birthdate: String, profileImg: String): String {
            val encodedProfileImgUrl = URLEncoder.encode(profileImg, StandardCharsets.UTF_8.toString())
            return "$route/$name/$nickname/$birthdate/$encodedProfileImgUrl"
        }
    }

    data object AddPost : Route {
        override val route: String = "AddPost"
        const val modeArg = "mode"
        const val editPostIdArg = "editPostId"
        const val editImagesArg = "editImages"
        const val editContentArg = "editContent"
        val routeWithArgs = "$route?$modeArg={$modeArg}&$editPostIdArg={$editPostIdArg}?$editImagesArg={$editImagesArg}$editContentArg={$editContentArg}"
        val arguments = listOf(
            navArgument(modeArg) {
                nullable = true
                type = NavType.IntType
            },
            navArgument(editPostIdArg) {
                nullable = true
                type = NavType.IntType
            },
            navArgument(editImagesArg) {
                nullable = true
                type = NavType.StringArrayType
            },
            navArgument(editContentArg) {
                nullable = true
                type = NavType.StringType
            }
        )

        fun getRoute(mode: Int, editPostId: Long, editImages: Array<String>, editContent: String): String {
            val endcodedImageUrls: Array<String> =
                editImages.map { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) }.toTypedArray()

            return "$route/$mode/$editPostId/$endcodedImageUrls/$editContent"
        }
    }
}
