package ru.practicum.android.diploma.favourite.domain.models

data class Vacancy(
    val vacancyId: String, // ID вакансии
    val vacancyName: String, // Название профессии
    val employer: String, // Работодатель
    val areaRegion: String, // Регион (в документации НН указано название города "Москва")
    val salary: String, // ЗП
    val artworkUrl: String, // Изображение
)
