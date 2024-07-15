package ru.practicum.android.diploma.favourite.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.favourite.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.vacancy.domain.models.Salary
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails

class FavoriteVacancyDbConverter(private val gson: Gson) {

    private val typeForSalary = object : TypeToken<Salary>() {}.type
    private val typeForKeySkills = object : TypeToken<List<String>>() {}.type

    fun map(vacancy: VacancyDetails): VacancyEntity {
        return VacancyEntity(
            vacancyIdInDatabase = vacancy.vacancyIdInDatabase,
            vacancyId = vacancy.vacancyId,
            vacancyName = vacancy.vacancyName,
            employerName = vacancy.employerName,
            employment = vacancy.employment,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            vacancyUrlHh = vacancy.vacancyUrlHh,
            salary = gson.toJson(vacancy.salary), // from Salary to String
            keySkills = gson.toJson(vacancy.keySkills), // from List<String> to String
            area = vacancy.area,
            description = vacancy.description,
            artworkUrl = vacancy.artworkUrl
        )
    }

    fun map(vacancy: VacancyEntity): VacancyDetails {
        return VacancyDetails(
            vacancyIdInDatabase = vacancy.vacancyIdInDatabase,
            vacancyId = vacancy.vacancyId,
            vacancyName = vacancy.vacancyName,
            employerName = vacancy.employerName,
            employment = vacancy.employment,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            vacancyUrlHh = vacancy.vacancyUrlHh,
            salary = gson.fromJson(vacancy.salary, typeForSalary), // from String to Salary
            keySkills = gson.fromJson(vacancy.keySkills, typeForKeySkills), // from String to List<String>
            area = vacancy.area,
            description = vacancy.description,
            artworkUrl = vacancy.artworkUrl,
            isFavorite = true
        )
    }
}
