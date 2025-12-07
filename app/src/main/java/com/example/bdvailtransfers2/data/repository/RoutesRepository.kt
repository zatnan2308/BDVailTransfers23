package com.example.bdvailtransfers2.data.repository

import com.example.bdvailtransfers2.data.model.Route
import com.example.bdvailtransfers2.data.network.BDVailApiService
import com.example.bdvailtransfers2.data.network.NetworkModule

/**
 * Репозиторий для списка маршрутов.
 * Держит простой кеш в памяти, чтобы не дёргать сеть лишний раз.
 */
class RoutesRepository(
    private val api: BDVailApiService = NetworkModule.apiService
) {

    private var cachedRoutes: List<Route>? = null

    /**
     * Получить маршруты.
     * @param forceRefresh если true — всегда лезем в сеть, игнорируя кеш.
     */
    suspend fun getRoutes(forceRefresh: Boolean = false): List<Route> {
        if (cachedRoutes == null || forceRefresh) {
            cachedRoutes = api.getRoutes()
        }
        return cachedRoutes.orEmpty()
    }

    /**
     * Можно добавить метод для поиска маршрута по id.
     */
    fun getRouteById(id: Long): Route? {
        return cachedRoutes?.firstOrNull { it.id == id }
    }
}
