package ru.practicum.android.diploma.util

import android.content.Context
import android.content.res.Resources.getSystem
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.Salary
import java.util.Currency

val Int.dp: Int get() = (this / getSystem().displayMetrics.density).toInt()

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()

fun Salary.salaryFormat(context: Context): String {
    var salaryString = ""

    if (this.from != null) salaryString += context.getString(R.string.from, this.from)
    if (this.to != null) salaryString += context.getString(R.string.to, this.to)

    val currency = Currency.getInstance(this.currency)
    salaryString += currency.symbol

    return salaryString
}
