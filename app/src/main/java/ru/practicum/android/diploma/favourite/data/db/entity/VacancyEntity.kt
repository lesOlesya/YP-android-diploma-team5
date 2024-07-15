package ru.practicum.android.diploma.favourite.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "vacancy_id_in_database")
    val vacancyIdInDatabase: Long, // ID вакансии в базе данных
    @ColumnInfo(name = "vacancy_id")
    val vacancyId: String, // ID вакансии на сервере
    @ColumnInfo(name = "vacancy_name")
    val vacancyName: String, // Название профессии
    @ColumnInfo(name = "employer_name")
    val employerName: String?, // Работодатель
    @ColumnInfo(name = "area")
    val area: String, // Регион
    @ColumnInfo(name = "description")
    val description: String, // Описание вакансии
    @ColumnInfo(name = "employment")
    val employment: String?, // Тип занятости
    @ColumnInfo(name = "schedule")
    val schedule: String?, // Расписание
    @ColumnInfo(name = "experience")
    val experience: String?, // Опыт работы
    @ColumnInfo(name = "salary")
    val salary: String?, // ЗП data class Salary в String(gson)
    @ColumnInfo(name = "key_skills")
    val keySkills: String, // Ключевые навыки List<String> в String(gson)
    @ColumnInfo(name = "artwork_url")
    val artworkUrl: String?, // Логотип компании
    @ColumnInfo(name = "vacancy_url_hh")
    val vacancyUrlHh: String, // Логотип компании
)
