package com.example.userinfoexplorer

import android.content.Context
import com.example.userinfoexplorer.data.network.RandomUserApi
import com.example.userinfoexplorer.data.UserRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Singletons {
    private lateinit var applicationContext: Context

//    val database: AppDatabase by lazy {
//        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db").build()
//    }

    private const val BASE_URL = "https://randomuser.me"

    private val retrofit: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val randomUserApi: RandomUserApi by lazy {
        retrofit.create(RandomUserApi::class.java)
    }

    val userRepository: UserRepository by lazy {
        UserRepository(randomUserApi)
    }

    fun init(context: Context) {
        applicationContext = context
    }
}