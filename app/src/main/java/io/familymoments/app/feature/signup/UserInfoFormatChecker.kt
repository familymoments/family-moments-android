package io.familymoments.app.feature.signup

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Locale

object UserInfoFormatChecker {
    fun checkEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun checkPassword(password: String): Boolean {
        val regex = "^[a-zA-Z0-9]{8,12}$"
        return password.matches(Regex(regex))
    }

    fun checkId(userId: String): Boolean {
        val regex = "^[a-zA-Z0-9]{6,12}$"
        return userId.matches(Regex(regex))
    }

    fun checkNickname(nickname: String): Boolean {
        val regex = "^[a-zA-Z0-9가-힣]{3,8}$"
        return nickname.matches(Regex(regex))
    }
}
