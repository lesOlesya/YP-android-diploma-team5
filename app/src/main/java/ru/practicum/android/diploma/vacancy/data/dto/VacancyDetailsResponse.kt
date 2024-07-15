package ru.practicum.android.diploma.vacancy.data.dto

import ru.practicum.android.diploma.search.data.dto.Response

class VacancyDetailsResponse(
    val id: String, // ID вакансии
    val name: String, // Название вакансии
    val salary: SalaryResponse, // ЗП
    val employer: EmployerResponse, // Работодатель
    val area: AreaResponse, // Регион
    val experience: ExperienceResponse, // Опыт работы
    val employment: EmploymentResponse, // Тип занятости
    val schedule: ScheduleResponse, // Расписание
    val description: String, // Описание вакансии
    val keySkills: List<KeySkillResponse>, // Ключевые навыки
    val alternateUrl: String, // Ссылка на вакансию на hh.ru
) : Response()

class AreaResponse(
    val name: String
)

class SalaryResponse(
    val from: Int,
    val to: Int,
    val currency: String
)

class EmployerResponse(
    val logoUrls: LogoUrlsResponse,
    val name: String,
)

class LogoUrlsResponse(
    val logo240: String,
    val logo90: String,
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
