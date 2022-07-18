package com.a90ms.searchbook.di

import com.a90ms.searchbook.BuildConfig
import com.a90ms.searchbook.network.GoogleApisService
import com.a90ms.searchbook.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    fun provideGoogleService(): GoogleApisService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient(AppInterceptor(),provideLoggingInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(GoogleApisService::class.java)
    }

    private fun provideOkHttpClient(
        interceptor: AppInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {


       return OkHttpClient.Builder()
            .run {
                addInterceptor(interceptor)
                addNetworkInterceptor(httpLoggingInterceptor)
                build()
            }
    }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val newRequest = request().newBuilder()
                .build()
            proceed(newRequest)
        }
    }


    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
}