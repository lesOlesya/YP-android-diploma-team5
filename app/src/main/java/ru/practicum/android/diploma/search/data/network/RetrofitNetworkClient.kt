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

    override suspend fun doRequest(dto: Any): Response {
        if (isOnline(context) == false) {
            return Response().apply { resultCode = NETWORK_ERROR }
        }

        if ((dto !is VacancySearchRequest) && (dto !is VacancyDetailsRequest)) {
            return Response().apply { resultCode = REQUEST_ERROR }
        }

        @Suppress("detekt.Throwable")
        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is VacancySearchRequest -> headHunterService.search(dto.expression)
                    else -> headHunterService.getVacancyDetails(vacancyId = (dto as VacancyDetailsRequest).vacancyId)
                }
                response.apply { resultCode = SUCCESS }
            } catch (e: Throwable) {
                Response().apply { resultCode = SERVER_ERROR }
            }
        }
    }

    companion object {
        private const val NETWORK_ERROR = -1
        private const val REQUEST_ERROR = 400
        private const val SERVER_ERROR = 500
        private const val SUCCESS = 200
    }
}
