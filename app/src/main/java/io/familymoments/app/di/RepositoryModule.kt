package io.familymoments.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.familymoments.app.core.network.UserService
import io.familymoments.app.repository.UserRepository
import io.familymoments.app.repository.TokenRepository
import io.familymoments.app.repository.impl.UserRepositoryImpl
import io.familymoments.app.repository.impl.TokenRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService, tokenRepository: TokenRepository): UserRepository {
        return UserRepositoryImpl(userService, tokenRepository)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(@ApplicationContext context: Context): TokenRepository {
        val preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return TokenRepositoryImpl(preferences)
    }

}
