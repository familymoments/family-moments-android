package io.familymoments.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.familymoments.app.BuildConfig
import io.familymoments.app.core.network.AuthErrorManager
import io.familymoments.app.core.network.AuthInterceptor
import io.familymoments.app.core.network.api.CommentService
import io.familymoments.app.core.network.api.FamilyService
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.api.SignInService
import io.familymoments.app.core.network.api.UserService
import io.familymoments.app.core.network.api.PostService
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    @DefaultRetrofit
    fun provideRetrofit(
        @DefaultOkHttpClient okHttpClient: OkHttpClient
    ): Retrofit = createRetrofit(okHttpClient)

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(
        @AuthOkHttpClient okHttpClient: OkHttpClient
    ): Retrofit = createRetrofit(okHttpClient)

    private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @DefaultOkHttpClient
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val request = chain.request().newBuilder().build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    @AuthOkHttpClient
    fun provideAuthOkHttpClient(userInfoPreferencesDataSource: UserInfoPreferencesDataSource): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .addInterceptor(AuthInterceptor(userInfoPreferencesDataSource))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthErrorManager(): AuthErrorManager {
        return AuthErrorManager()
    }

    @Provides
    @Singleton
    fun provideUserService(@AuthRetrofit retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideSignInService(@DefaultRetrofit retrofit:Retrofit):SignInService{
        return retrofit.create(SignInService::class.java)
    }

    @Provides
    @Singleton
    fun providePostService(@AuthRetrofit retrofit: Retrofit): PostService {
        return retrofit.create(PostService::class.java)
    }

    @Provides
    @Singleton
    fun provideCommentService(@AuthRetrofit retrofit: Retrofit):CommentService{
        return retrofit.create(CommentService::class.java)
    }

    @Provides
    @Singleton
    fun provideFamilyService(@AuthRetrofit retrofit: Retrofit): FamilyService {
        return retrofit.create(FamilyService::class.java)
    }
}
