package ru.practicum.android.diploma.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkHelper {
    companion object {
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return connectivityManager != null
        }
    }

}
