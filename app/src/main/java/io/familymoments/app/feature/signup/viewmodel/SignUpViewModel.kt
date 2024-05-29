package io.familymoments.app.feature.signup.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.dto.request.UserJoinReq
import io.familymoments.app.core.network.repository.SignInRepository
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.signup.UserInfoFormatChecker
import io.familymoments.app.feature.signup.mapper.toRequest
import io.familymoments.app.feature.signup.mapper.toUserJoinReq
import io.familymoments.app.feature.signup.uistate.SignUpInfoUiState
import io.familymoments.app.feature.signup.uistate.SignUpUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signInRepository: SignInRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

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
                    emailFormValidated = UserInfoFormatChecker.checkEmail(
                        email
                    )
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

    fun checkBirthDayFormat(birthDay: String) {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    birthDayFormValidated = UserInfoFormatChecker.checkBirthDay(
                        birthDay
                    )
                )
            )
        }
    }

    fun checkIdDuplication(id: String) {
        async(
            operation = { signInRepository.checkId(id) },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        signUpValidatedUiState = it.signUpValidatedUiState.copy(
                            userIdDuplicatedUiState = it.signUpValidatedUiState.userIdDuplicatedUiState.copy(
                                isSuccess = true,
                                duplicatedPass = true
                            )
                        )
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        signUpValidatedUiState = it.signUpValidatedUiState.copy(
                            userIdDuplicatedUiState = it.signUpValidatedUiState.userIdDuplicatedUiState.copy(
                                isSuccess = false,
                                duplicatedPass = false
                            )
                        )
                    )
                }
            })
    }

    fun checkEmailDuplication(email: String) {
        async(
            operation = { signInRepository.checkEmail(email) },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        signUpValidatedUiState = it.signUpValidatedUiState.copy(
                            emailDuplicatedUiState = it.signUpValidatedUiState.emailDuplicatedUiState.copy(
                                isSuccess = true,
                                duplicatedPass = true
                            )
                        )
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        signUpValidatedUiState = it.signUpValidatedUiState.copy(
                            emailDuplicatedUiState = it.signUpValidatedUiState.emailDuplicatedUiState.copy(
                                isSuccess = false,
                                duplicatedPass = false
                            )
                        )
                    )
                }
            }
        )
    }

    fun resetUserIdDuplicatedPass() {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    userIdDuplicatedUiState = it.signUpValidatedUiState.userIdDuplicatedUiState.copy(
                        duplicatedPass = false
                    )
                )
            )
        }
    }

    fun resetEmailDuplicatedPass() {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    emailDuplicatedUiState = it.signUpValidatedUiState.emailDuplicatedUiState.copy(
                        duplicatedPass = false
                    )
                )
            )
        }
    }

    fun resetUserIdDuplicatedSuccess() {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    userIdDuplicatedUiState = it.signUpValidatedUiState.userIdDuplicatedUiState.copy(
                        isSuccess = null
                    )
                )
            )
        }
    }

    fun resetEmailDuplicatedSuccess() {
        _uiState.update {
            it.copy(
                signUpValidatedUiState = it.signUpValidatedUiState.copy(
                    emailDuplicatedUiState = it.signUpValidatedUiState.emailDuplicatedUiState.copy(
                        isSuccess = null
                    )
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
                async(
                    operation = {
                        userRepository.executeSocialSignIn(socialType, socialToken)
                    },
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                signUpResultUiState = it.signUpResultUiState.copy(
                                    isSuccess = true
                                )
                            )
                        }
                    },
                    onFailure = {
                        _uiState.update {
                            it.copy(
                                signUpResultUiState = it.signUpResultUiState.copy(
                                    isSuccess = false,
                                    message = "회원가입에 성공했으나 로그인에 실패했습니다. 다시 시도해주세요."
                                )
                            )
                        }
                    }
                )
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        signUpResultUiState = it.signUpResultUiState.copy(
                            isSuccess = false
                        )
                    )
                }
            }
        )
    }

    fun resetSignUpResultSuccess() {
        _uiState.update {
            it.copy(
                signUpResultUiState = it.signUpResultUiState.copy(
                    isSuccess = null
                )
            )
        }
    }
}
