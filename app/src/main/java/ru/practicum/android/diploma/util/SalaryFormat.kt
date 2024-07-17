package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.Salary
import java.text.DecimalFormat
import java.util.Currency

fun Salary.salaryFormat(context: Context): String? {
    var salaryString = ""

    if (this.from != null) salaryString += context.getString(R.string.from, this.from.sumFormat())
    if (this.to != null) salaryString += context.getString(R.string.to, this.to.sumFormat())

    val currency: Currency = when (this.currency) {
        null -> return null
        "RUR" -> Currency.getInstance("RUB")
        else -> Currency.getInstance(this.currency)
    }

    salaryString += currency.symbol

    return salaryString
}

private fun Int.sumFormat(): String {
    return DecimalFormat("#,###.##")
        .format(this)
        .replace(",", " ")
}