package ru.practicum.android.diploma.search.data.dto

data class VacancyDto(
    val id: String, // ID вакансии
    val name: String, // Название вакансии
    val salary: SalaryResponse, // ЗП
    val employer: EmployerResponse, // Работодатель
    val area: AreaResponse, // Регион
)

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
