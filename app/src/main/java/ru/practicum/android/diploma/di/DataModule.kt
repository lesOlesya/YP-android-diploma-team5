package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.favourite.data.db.AppDatabase
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.network.ApiConstants
import ru.practicum.android.diploma.search.data.network.HeadHunterApi
import ru.practicum.android.diploma.search.data.network.RetrofitNetworkClient
import java.util.concurrent.TimeUnit

val dataModule = module {

    single {
        androidContext()
            .getSharedPreferences("YP_HH_preferences", Context.MODE_PRIVATE)
    }

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

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single<HeadHunterApi> {
        get<Retrofit>().create(HeadHunterApi::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }


}
