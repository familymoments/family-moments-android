package io.familymoments.app.feature.choosingfamily.route

sealed interface ChoosingFamilyRoute {
    val route: String

    data object Start : ChoosingFamilyRoute {
        override val route = "Start"
    }

    data object SearchMember : ChoosingFamilyRoute {
        override val route = "SearchMember"
    }

    data object SetProfile : ChoosingFamilyRoute {
        override val route = "SetProfile"
    }

    data object CopyInvitationLink : ChoosingFamilyRoute {
        override val route = "CopyInvitationLink"
        const val inviteLinkStringArgs = "inviteLink"
        val routeWithArgs = "$route/{${inviteLinkStringArgs}}"
        fun getRoute(inviteLink: String): String = "$route/$inviteLink"
    }

    data object Join : ChoosingFamilyRoute {
        override val route = "Join"
    }

    data object SetAlarm : ChoosingFamilyRoute {
        override val route = "SetAlarm"
    }
}
