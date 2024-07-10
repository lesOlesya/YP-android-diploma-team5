package ru.practicum.android.diploma.di

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.search.data.network.api.ApiConstants
import ru.practicum.android.diploma.search.data.network.api.NetworkInterface
import java.util.concurrent.TimeUnit

val remoteModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(get<GsonConverterFactory>())
            .client(get<OkHttpClient>())
            .build()
    }

    single<GsonConverterFactory>{
        GsonConverterFactory.create()
    }

    single<NetworkInterface> {
        get<Retrofit>().create(NetworkInterface::class.java)
    }
}
