package io.familymoments.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.familymoments.app.core.network.api.AuthService
import io.familymoments.app.core.network.datasource.TokenPreferencesDataSource
import io.familymoments.app.core.network.datasource.TokenPreferencesDataSourceImpl
import io.familymoments.app.core.network.repository.AuthRepository
import io.familymoments.app.core.network.repository.impl.AuthRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService, tokenPreferencesDataSource: TokenPreferencesDataSource): AuthRepository {
        return AuthRepositoryImpl(authService, tokenPreferencesDataSource)
    }

    @Provides
    @Singleton
    fun provideTokenPreferencesDataSource(@ApplicationContext context: Context): TokenPreferencesDataSource {
        val preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return TokenPreferencesDataSourceImpl(preferences)
    }
}
