package com.example.moviesapp.di

import com.example.moviesapp.data.api.ApiInterface
import com.example.moviesapp.data.api.ConnectionManager
import com.example.moviesapp.data.api.ConnectionManagerContract
import com.example.moviesapp.data.api.interceptor.NetworkStatusInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.themoviedb.org/"
    private const val TOKEN =
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0MTFiZTY2MTk0ZDIzZWJmMDRjN2I4ZTk1MzQ0ZTY3MSIsInN1YiI6IjY0ZDc2NmJkZjE0ZGFkMDBjNmY4OGQ4ZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.r4qkY6rn45KEFcCK4j2N64pCVOKnqFuvBO2k5C6I_p4"

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
    }

    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    fun provideConnectionManager(connectionManager: ConnectionManager): ConnectionManagerContract =
        connectionManager


    @Provides
    fun provideOkHttpClient(networkStatusInterceptor: NetworkStatusInterceptor): OkHttpClient {
        val b = OkHttpClient.Builder()
        b.addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .also {
                        it.addHeader("accept", "application/json")
                        it.addHeader("Authorization", TOKEN)
                    }
                    .build()
            )
        }.also { builder: OkHttpClient.Builder ->
            builder.connectTimeout(60, TimeUnit.SECONDS)
            builder.readTimeout(60, TimeUnit.SECONDS)
            builder.writeTimeout(60, TimeUnit.SECONDS)

            val interceptor = HttpLoggingInterceptor()
            //adds logs to logcat
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.apply {
                addInterceptor(networkStatusInterceptor)
                addInterceptor(interceptor)
            }
        }

        return b.build()
    }
}