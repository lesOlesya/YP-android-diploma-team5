package ru.practicum.android.diploma.vacancy.data.dto

import ru.practicum.android.diploma.search.data.dto.Response

@Suppress("detekt.LongParameterList")
class VacancyDetailsResponse(
    val id: String, // ID вакансии
    val name: String, // Название вакансии
    val salary: SalaryResponse?, // ЗП
    val employer: EmployerResponse, // Работодатель
    val area: AreaResponse, // Регион
    val experience: ExperienceResponse?, // Опыт работы
    val employment: EmploymentResponse?, // Тип занятости
    val schedule: ScheduleResponse?, // Расписание
    val description: String, // Описание вакансии
    val key_skills: List<KeySkillResponse>?, // Ключевые навыки
    val alternate_url: String, // Ссылка на вакансию на hh.ru
) : Response()

class AreaResponse(
    val name: String
)

class SalaryResponse(
    val from: Int?,
    val to: Int?,
    val currency: String
)

class EmployerResponse(
    val logo_urls: LogoUrlsResponse?,
    val name: String,
)

class LogoUrlsResponse(
    val `240`: String,
    val `90`: String,
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
