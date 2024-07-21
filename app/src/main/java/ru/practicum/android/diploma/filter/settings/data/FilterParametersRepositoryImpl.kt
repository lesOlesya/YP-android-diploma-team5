package ru.practicum.android.diploma.filter.settings.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.filter.settings.domain.api.FilterParametersRepository
import ru.practicum.android.diploma.filter.settings.domain.models.FilterParameters

const val FILTER_PARAMETERS_KEY = "key_for_filter_parameters"

class FilterParametersRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : FilterParametersRepository {

    override fun saveParameters(parameters: FilterParameters) {
        val json = gson.toJson(parameters)
        sharedPreferences.edit()
            .putString(FILTER_PARAMETERS_KEY, json)
            .apply()
    }

    override fun getParameters(): FilterParameters {
        val json = sharedPreferences.getString(FILTER_PARAMETERS_KEY, null) ?: return FilterParameters()
        val type = object : TypeToken<FilterParameters>() {}.type
        val parameters = gson.fromJson<FilterParameters>(json, type)
        return parameters
    }

}
