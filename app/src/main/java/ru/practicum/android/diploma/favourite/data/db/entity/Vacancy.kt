package ru.practicum.android.diploma.favourite.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "vacancy_table")
data class Vacancy (
    @PrimaryKey(autoGenerate = true)
    val id: Int
): Parcelable
