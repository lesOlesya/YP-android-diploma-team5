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

        from?.let { this.append(context.getString(R.string.from, it.sumFormat())) }

        to?.let { this.append(context.getString(R.string.to, it.sumFormat())) }

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
