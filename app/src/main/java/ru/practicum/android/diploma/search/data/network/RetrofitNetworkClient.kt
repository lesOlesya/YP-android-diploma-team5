package ru.practicum.android.diploma.search.data.network

//    import kotlinx.coroutines.Dispatchers
//    import kotlinx.coroutines.withContext
//    import ru.practicum.android.diploma.search.data.NetworkClient
//    import ru.practicum.android.diploma.search.data.dto.Response
//    import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
//
//    class RetrofitNetworkClient(
//        private val headHunterService: HeadHunterApi
//    ) : NetworkClient {
//
//        override suspend fun doRequest(dto: Any): Response {
//            if (isOnline() == false) { потом сделать проверку подключения с помощью NetworkHelper
//                return Response().apply { resultCode = -1 }
//            }
//            if (dto !is VacancySearchRequest) {
//                return Response().apply { resultCode = 400 }
//            }
//
//            return withContext(Dispatchers.IO) {
//                try {
//                    val response = headHunterService.search(dto.expression)
//                    response.apply { resultCode = 200 }
//                } catch (e: Throwable) {
//                    Response().apply { resultCode = 500 }
//                }
//            }
//        }
//    }
