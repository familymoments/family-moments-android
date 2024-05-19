package io.familymoments.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.familymoments.app.core.network.AuthErrorManager
import io.familymoments.app.core.network.api.CommentService
import io.familymoments.app.core.network.api.FamilyService
import io.familymoments.app.core.network.api.PostService
import io.familymoments.app.core.network.api.SignInService
import io.familymoments.app.core.network.api.UserService
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSourceImpl
import io.familymoments.app.core.network.repository.CommentRepository
import io.familymoments.app.core.network.repository.FamilyRepository
import io.familymoments.app.core.network.repository.PostRepository
import io.familymoments.app.core.network.repository.SignInRepository
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.core.network.repository.impl.CommentRepositoryImpl
import io.familymoments.app.core.network.repository.impl.FamilyRepositoryImpl
import io.familymoments.app.core.network.repository.impl.PostRepositoryImpl
import io.familymoments.app.core.network.repository.impl.SignInRepositoryImpl
import io.familymoments.app.core.network.repository.impl.UserRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    private const val USER_INFO_PREFERENCES_KEY = "user_info"

    @Provides
    @Singleton
    fun provideUserRepository(
        userService: UserService,
        userInfoPreferencesDataSource: UserInfoPreferencesDataSource,
        authErrorManager: AuthErrorManager
    ): UserRepository {
        return UserRepositoryImpl(userService, userInfoPreferencesDataSource, authErrorManager)
    }

    @Provides
    @Singleton
    fun provideUserInfoPreferencesDataSource(@ApplicationContext context: Context): UserInfoPreferencesDataSource {
        val preferences = context.getSharedPreferences(USER_INFO_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return UserInfoPreferencesDataSourceImpl(preferences)
    }

    @Provides
    @Singleton
    fun provideSignInRepository(signInService: SignInService): SignInRepository {
        return SignInRepositoryImpl(signInService)
    }

    @Provides
    @Singleton
    fun providePostRepository(postService: PostService): PostRepository {
        return PostRepositoryImpl(postService)
    }

    @Provides
    @Singleton
    fun provideCommentRepository(commentService: CommentService): CommentRepository {
        return CommentRepositoryImpl(commentService)
    }

    @Provides
    @Singleton
    fun provideFamilyRepository(
        familyService: FamilyService,
        userInfoPreferencesDataSource: UserInfoPreferencesDataSource,
    ): FamilyRepository {
        return FamilyRepositoryImpl(familyService, userInfoPreferencesDataSource)
    }
}
