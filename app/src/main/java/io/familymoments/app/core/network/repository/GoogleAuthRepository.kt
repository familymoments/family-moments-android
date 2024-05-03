package io.familymoments.app.core.network.repository

import android.content.Intent
import kotlinx.coroutines.flow.Flow


interface GoogleAuthRepository {
    fun signIn(callback: (Intent) -> Unit?): Flow<Result<String?>>
    fun signOut(): Flow<Boolean>
}
