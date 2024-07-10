package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.favourite.data.db.AppDatabase

val dataModule = module {

    single {
        androidContext()
            .getSharedPreferences("YP_HH_preferences", Context.MODE_PRIVATE)
    }

//    single<HeadHunterApi> {
//        Retrofit.Builder()
//            .baseUrl("https://api.hh.ru")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(HeadHunterApi::class.java)
//    }

//    single<NetworkClient> {
//        RetrofitNetworkClient(get(), androidContext())
//    }

   single {
       Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
           .fallbackToDestructiveMigration()
           .build()
    }

    factory { Gson() }

}
