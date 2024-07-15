package ru.practicum.android.diploma.search.data.dto

import ru.practicum.android.diploma.search.domain.models.Salary

data class VacancyDto(
    val vacancyId: Int,
    val vacancyName: String, // Название вакансии
    val salary: Salary?, // ЗП
    val employerName: String?, // Работодатель
    val area: String, // Регион
    val artworkUrl: String?, // Логотип компании
    // данные для деталей
    val vacancyIdInDatabase: Long = 0L, // ID вакансии в БД
    val experience: String? = null, // Опыт работы
    val employment: String? = null, // Тип занятости
    val schedule: String? = null, // Расписание
    val description: String? = null, // Описание вакансии
    val keySkills: List<String>? = null, // Ключевые навыки
    val vacancyUrlHh: String? = null, // Ссылка на вакансию на hh.ru
    val isFavorite: Boolean = false,
)
