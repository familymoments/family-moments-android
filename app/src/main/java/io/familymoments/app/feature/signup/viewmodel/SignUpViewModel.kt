package io.familymoments.app.feature.signup.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.SignInRepository
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.signup.UserInfoFormatChecker
import io.familymoments.app.feature.signup.mapper.toRequest
import io.familymoments.app.feature.signup.mapper.toUserJoinReq
import io.familymoments.app.feature.signup.uistate.SignUpInfoUiState
import io.familymoments.app.feature.signup.uistate.SignUpUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signInRepository: SignInRepository,
    private val userRepository: UserRepository,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()
    private var timerJob: Job? = null

    fun checkIdFormat(id: String) {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    userIdFormValidated = UserInfoFormatChecker.checkId(
                        id
                    )
                )
            )
        }
    }

    fun checkPasswordFormat(password: String) {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    passwordFormValidated = UserInfoFormatChecker.checkPassword(
                        password
                    )
                )
            )
        }
    }

    fun checkEmailFormat(email: String) {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    emailFormValidated = UserInfoFormatChecker.checkEmail(email)
                )
            )
        }
    }

    fun checkNicknameFormat(nickname: String) {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    nicknameFormValidated = UserInfoFormatChecker.checkNickname(
                        nickname
                    )
                )
            )
        }
    }

    fun checkIdDuplication(id: String) {
        async(
            operation = { signInRepository.checkId(id) },
            onSuccess = { result ->
                _uiState.update {
                    it.copy(
                        postSuccess = true,
                        message = result.result,
                        signUpValidatedUiState = it.signUpValidatedUiState.copy(
                            userIdDuplicatedPass = true
                        )
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        postSuccess = false,
                        message = error.message ?: "",
                        signUpValidatedUiState = it.signUpValidatedUiState.copy(
                            userIdDuplicatedPass = false
                        )
                    )
                }
            })
    }

    fun sendEmailVerificationCode(email: String) {
        async(
            operation = {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        verificationCodeButtonUiState = it.verificationCodeButtonUiState.copy(
                            sendEmailAvailable = false,
                        )
                    )
                }
                resetExpirationTimer()
                signInRepository.sendEmailVerificationCode(email)
            },
            onSuccess = { result ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        postSuccess = true,
                        message = "입력하신 이메일로 인증 번호가 발송되었습니다.",
                        verificationCodeButtonUiState = it.verificationCodeButtonUiState.copy(
                            sendEmailAvailable = true
                        )
                    )
                }
                startExpirationTimer(seconds = 60 * 3)
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        postSuccess = false,
                        message = error.message ?: "",
                        verificationCodeButtonUiState = it.verificationCodeButtonUiState.copy(
                            sendEmailAvailable = true
                        )
                    )
                }
            }
        )
    }

    fun verifyEmailVerificationCode(code: String) {
        async(
            operation = {
                _uiState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                signInRepository.verifyEmailVerificationCode(_uiState.value.signUpInfoUiState.email, code)

            },
            onSuccess = { result ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        postSuccess = true,
                        message = result.result,
                        signUpValidatedUiState = it.signUpValidatedUiState.copy(
                            emailVerified = true,
                        ),
                        verificationCodeButtonUiState = it.verificationCodeButtonUiState.copy(
                            verifyCodeAvailable = false
                        )
                    )
                }
                resetExpirationTimer()
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        postSuccess = false,
                        message = error.message ?: "",
                        signUpValidatedUiState = it.signUpValidatedUiState.copy(
                            emailVerified = false
                        ),
                        verificationCodeButtonUiState = it.verificationCodeButtonUiState.copy(
                            verifyCodeAvailable = true
                        )
                    )
                }
            }
        )
    }

    fun resetEmailVerified() {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    emailVerified = false
                )
            )
        }
    }

    fun resetVerifyCodeAvailable(){
        _uiState.update {
            it.copy(
                verificationCodeButtonUiState = it.verificationCodeButtonUiState.copy(
                    verifyCodeAvailable = true
                )
            )
        }
    }

    private fun cancelTimerJob() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resetPostSuccess() {
        _uiState.update {
            it.copy(
                postSuccess = null
            )
        }
    }

    fun resetUserIdDuplicatedPass() {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    userIdDuplicatedPass = false
                )
            )
        }
    }

    fun executeSignUp(signUpInfoUiState: SignUpInfoUiState, socialType: String = "", socialToken: String = "") {
        check(signUpInfoUiState.imgFile != null) { throw NullPointerException() }
        val imageRequestBody = signUpInfoUiState.imgFile.asRequestBody("image/*".toMediaTypeOrNull())
        val profileImgPart =
            MultipartBody.Part.createFormData("profileImg", signUpInfoUiState.imgFile.name, imageRequestBody)
        async(
            operation = {
                if (socialType.isNotEmpty()) {
                    signInRepository.executeSocialSignUp(profileImgPart, signUpInfoUiState.toUserJoinReq(socialType))
                } else {
                    signInRepository.executeSignUp(profileImgPart, signUpInfoUiState.toRequest())
                }
            },
            onSuccess = {
                if (socialType.isNotEmpty()) {
                    async(
                        operation = {
                            val fcmToken = userInfoPreferencesDataSource.loadFCMToken()
                            userRepository.executeSocialSignIn(socialType, socialToken, fcmToken)
                        },
                        onSuccess = {
                            _uiState.update {
                                it.copy(
                                    signUpSuccess = true,
                                )
                            }
                        },
                        onFailure = {
                            _uiState.update {
                                it.copy(
                                    signUpSuccess = false,
                                    message = "회원가입에 성공했으나 로그인에 실패했습니다. 다시 시도해주세요.",
                                )
                            }
                        }
                    )
                } else {
                    _uiState.update {
                        it.copy(
                            signUpSuccess = true
                        )
                    }
                }

            },
            onFailure = { throwable ->
                _uiState.update {
                    it.copy(
                        signUpSuccess = false,
                        message = throwable.message ?: ""
                    )
                }
            }
        )
    }

    fun resetSignUpResultSuccess() {
        _uiState.update {
            it.copy(
                signUpSuccess = null
            )
        }
    }

    fun updateSignUpInfo(signUpInfoUiState: SignUpInfoUiState) {
        _uiState.update {
            it.copy(
                signUpInfoUiState = signUpInfoUiState
            )
        }
    }

    fun checkPasswordSame(password: String) {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    passwordSameCheck = it.signUpInfoUiState.password == password
                )
            )
        }
    }

    private fun resetExpirationTimer() {
        _uiState.update {
            it.copy(
                expirationTimeUiState = it.expirationTimeUiState.copy(
                    isExpirationTimeVisible = false
                )
            )
        }
        cancelTimerJob()
    }

    private fun startExpirationTimer(seconds: Int) {
        timerJob = viewModelScope.launch {
            for (remainingTime in seconds downTo 0) {
                _uiState.update {
                    it.copy(
                        expirationTimeUiState = it.expirationTimeUiState.copy(
                            isExpirationTimeVisible = remainingTime > 0,
                            expirationTime = remainingTime
                        )
                    )
                }
                delay(1000)
            }
        }
    }

}
