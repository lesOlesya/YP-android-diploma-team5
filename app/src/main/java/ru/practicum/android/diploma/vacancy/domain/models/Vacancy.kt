package ru.practicum.android.diploma.vacancy.domain.models

data class Vacancy(
    val vacancyId: String,
    val vacancyName: String, // Название профессии
    val employer: String, // Работодатель
    val areaRegion: String, // Регион
    val salary: String, // ЗП
    val artworkUrl: String, // Лого компании, видимо
)
