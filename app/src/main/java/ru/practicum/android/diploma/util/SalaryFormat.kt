package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.Salary
import java.text.DecimalFormat
import java.util.Currency

fun Salary.salaryFormat(context: Context): String? {

    val from = this.from
    val to = this.to
    val salaryCurrency = this.currency
    val salaryString = buildString {

        if (from != null) {
            this.append(context.getString(R.string.from, from.sumFormat()))
        }
        if (to != null) {
            this.append(context.getString(R.string.to, to.sumFormat()))
        }
        val currency: Currency = when (salaryCurrency) {
            null -> return null
            "RUR" -> Currency.getInstance("RUB")
            else -> Currency.getInstance(salaryCurrency)
        }
        this.append(currency.symbol)
    }
    return salaryString
}

private fun Int.sumFormat(): String {
    return DecimalFormat("#,###.##")
        .format(this)
        .replace(",", " ")
}
