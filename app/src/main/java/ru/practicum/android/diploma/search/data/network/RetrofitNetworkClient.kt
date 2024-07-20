package ru.practicum.android.diploma.search.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.search.data.NetworkClient
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.util.NetworkHelper.isOnline
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsRequest

class RetrofitNetworkClient(
    private val headHunterService: HeadHunterApi,
    private val context: Context,
) : NetworkClient {

    @Suppress("detekt.ReturnCount")
    override suspend fun doRequest(dto: Any): Response {
        if (isOnline(context) == false) {
            return Response().apply { resultCode = ErrorMessageConstants.NETWORK_ERROR }
        }

        if (dto !is VacancySearchRequest && dto !is VacancyDetailsRequest) {
            return Response().apply { resultCode = ErrorMessageConstants.REQUEST_ERROR }
        }

        return requestWithContext(dto)
    }

    @Suppress("detekt.TooGenericExceptionCaught", "detekt.SwallowedException")
    private suspend fun requestWithContext(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is VacancySearchRequest -> headHunterService.search(text = dto.expression, page = dto.page)
                    else -> headHunterService.getVacancyDetails(vacancyId = (dto as VacancyDetailsRequest).vacancyId)
                }
                response.apply { resultCode = ErrorMessageConstants.SUCCESS }
            } catch (e: Throwable) {
                Response().apply { resultCode = ErrorMessageConstants.SERVER_ERROR }
            }
        }
    }
}
