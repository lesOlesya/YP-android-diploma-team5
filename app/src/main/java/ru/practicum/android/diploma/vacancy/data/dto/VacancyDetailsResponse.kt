package ru.practicum.android.diploma.vacancy.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.search.data.dto.Response

@Suppress("detekt.LongParameterList")
class VacancyDetailsResponse(
    val id: String, // ID вакансии
    val name: String, // Название вакансии
    val salary: SalaryResponse?, // ЗП
    val employer: EmployerResponse, // Работодатель
    val area: AreaResponse, // Регион
    val address: AddressResponse?, // Адрес
    val experience: ExperienceResponse?, // Опыт работы
    val employment: EmploymentResponse?, // Тип занятости
    val schedule: ScheduleResponse?, // Расписание
    val description: String, // Описание вакансии
    @SerializedName("key_skills") val keySkills: List<KeySkillResponse>?, // Ключевые навыки
    @SerializedName("alternate_url") val alternateUrl: String, // Ссылка на вакансию на hh.ru
) : Response()

class AreaResponse(
    val name: String
)

class AddressResponse(
    val city: String?,
    @SerializedName("raw") val fullAddress: String?,
)

class SalaryResponse(
    val from: Int?,
    val to: Int?,
    val currency: String
)

class EmployerResponse(
    @SerializedName("logo_urls") val logoUrls: LogoUrlsResponse?,
    val name: String,
)

class LogoUrlsResponse(
    @SerializedName("240") val logo240: String,
    @SerializedName("90") val logo90: String,
    val original: String
)

class EmploymentResponse(
    val name: String
)

class ExperienceResponse(
    val name: String
)

class KeySkillResponse(
    val name: String
)

class ScheduleResponse(
    val name: String
)
