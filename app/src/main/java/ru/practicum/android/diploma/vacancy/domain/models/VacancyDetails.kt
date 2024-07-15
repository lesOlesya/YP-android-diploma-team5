package ru.practicum.android.diploma.vacancy.domain.models

data class VacancyDetails(
    val vacancyId: String, // ID вакансии
    val vacancyIdInDatabase: Long = 0L, // ID вакансии в БД
    val vacancyName: String, // Название вакансии
    val salary: Salary?, // ЗП
    val employerName: String?, // Работодатель
    val area: String, // Регион
    val experience: String?, // Опыт работы
    val employment: String?, // Тип занятости
    val schedule: String?, // Расписание
    val description: String, // Описание вакансии
    val keySkills: List<String>, // Ключевые навыки
    val vacancyUrlHh: String, // Ссылка на вакансию на hh.ru
    val artworkUrl: String?, // Логотип компании
    val isFavorite: Boolean = false, // В избранном
)

class Salary(
    val from: Int,
    val to: Int,
    val currency: String
)
