@file:Suppress("DEPRECATION")

package io.familymoments.app.core.network.repository.impl

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.familymoments.app.BuildConfig
import io.familymoments.app.core.network.repository.GoogleAuthRepository
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GoogleAuthRepositoryImpl @Inject constructor(private val context: Context): GoogleAuthRepository {


    private lateinit var oldClient: GoogleSignInClient
    private lateinit var credentialManager: CredentialManager
    private lateinit var request: GetCredentialRequest

    init {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .requestEmail()
                .build()
            oldClient = GoogleSignIn.getClient(context, gso)
        } else {
            credentialManager = CredentialManager.create(context)

            val getGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .build()

            request = GetCredentialRequest(listOf(getGoogleIdOption))
        }
    }

    override fun signIn(callback: (Intent) -> Unit?) = flow {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val account = GoogleSignIn.getLastSignedInAccount(context)
            if (account != null) {
                // 이미 로그인 되어 있는 경우
                emit(Result.success(account.idToken))
            }
            callback(oldClient.signInIntent)
            emit(Result.failure(Exception("Google ID token not found")))
        } else {
            emit(newLogin())
        }
    }

    override fun signOut(): Flow<Boolean> {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            callbackFlow {
                oldClient.signOut().addOnCompleteListener {
                    trySendBlocking(true)
                }.addOnFailureListener {
                    trySendBlocking(false)
                }
            }
        } else {
            flow {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                emit(true)
            }
        }
    }



    // android 14 이상인 경우에만 실행
    private suspend fun newLogin(): Result<String> {

        runCatching {
            credentialManager.getCredential(context, request)
        }.onSuccess {
            when(val credential = it.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        return try {
                            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                            Timber.d("Google ID token: ${googleIdToken.idToken}")
                            Toast.makeText(context, "Google Login Success", Toast.LENGTH_SHORT).show()
                            Result.success(googleIdToken.idToken)
                        } catch (e: GoogleIdTokenParsingException) {
                            Timber.e("Received invalid Google ID token: ${e.message}")
                            Result.failure(e)
                        }
                    }
                }
            }
            return Result.failure(Exception("Google ID token not found"))
        }.onFailure { it.printStackTrace() }
        return Result.failure(Exception("Google ID token not found"))
    }
}
