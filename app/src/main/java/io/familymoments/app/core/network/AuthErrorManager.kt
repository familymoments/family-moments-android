package io.familymoments.app.core.network

import io.familymoments.app.core.util.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthErrorManager {

    private val _needReissueToken = MutableSharedFlow<Event<suspend () -> Flow<Resource<*>>>>()
    val needReissueToken = _needReissueToken.asSharedFlow()

    private val _needNavigateToLogin = MutableSharedFlow<Event<Unit>>()
    val needNavigateToLogin = _needNavigateToLogin.asSharedFlow()

    suspend fun emitNeedReissueToken(operation: suspend () -> Flow<Resource<*>>) {
        _needReissueToken.emit(Event(operation))
    }

    suspend fun emitNeedNavigateToLogin() {
        _needNavigateToLogin.emit(Event(Unit))
    }
}
