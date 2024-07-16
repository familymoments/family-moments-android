package io.familymoments.app.core.network

object HttpResponse {
    const val SUCCESS = 200
    const val ACCESS_DENIED = 403
    const val NO_POST = 404
    const val ACCESS_TOKEN_EXPIRED = 461
    const val REFRESH_TOKEN_EXPIRED = 471
    const val INTERNAL_SERVER_ERROR = 500
    const val INCORRECT_CURRENT_PASSWORD = 4000
    const val NEW_PASSWORD_SAME_AS_CURRENT = 4003
}

object HttpResponseMessage {
    const val ACCESS_DENIED_403 = "권한이 없는 유저의 접근입니다."
    const val NO_POST_404 = "post가 존재하지 않습니다."
    const val NO_COMMENTS_404 = "댓글이 존재하지 않습니다."
    const val NO_POST_LOVES_404 = "좋아요가 존재하지 않습니다."
    const val ACCESS_TOKEN_EXPIRED_461 = "Access Token의 기한이 만료되었습니다. 재발급 API를 호출해주세요"
    const val USER_NOT_IN_FAMILY_404 = "해당 유저가 해당 가족에 존재하지 않습니다."
    const val FAMILY_NOT_EXIST_404 = "존재하지 않는 가족입니다."
}
