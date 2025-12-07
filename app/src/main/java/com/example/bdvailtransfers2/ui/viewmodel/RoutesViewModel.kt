package com.example.bdvailtransfers2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bdvailtransfers2.data.model.Route
import com.example.bdvailtransfers2.data.repository.RoutesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Состояние экрана со списком маршрутов.
 */
sealed class RoutesUiState {
    /** Загрузка с сервера / из кеша */
    object Loading : RoutesUiState()

    /** Успешно получили список маршрутов */
    data class Success(val routes: List<Route>) : RoutesUiState()

    /** Ошибка сети / сервера / парсинга */
    data class Error(val message: String) : RoutesUiState()
}

/**
 * ViewModel для экранов, где нужен список маршрутов:
 * - Home (позже можно заменить заглушки)
 * - RoutesScreen
 * - BookingFormScreen (подтягивать детали маршрута по id).
 */
class RoutesViewModel(
    private val routesRepository: RoutesRepository = RoutesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<RoutesUiState>(RoutesUiState.Loading)
    val uiState: StateFlow<RoutesUiState> = _uiState.asStateFlow()

    init {
        // При первом создании VM сразу пробуем загрузить маршруты
        loadRoutes()
    }

    /**
     * Загрузить маршруты.
     * @param forceRefresh если true — игнорируем кеш в репозитории.
     */
    fun loadRoutes(forceRefresh: Boolean = false) {
        _uiState.value = RoutesUiState.Loading

        viewModelScope.launch {
            try {
                val routes = routesRepository.getRoutes(forceRefresh)
                if (routes.isEmpty()) {
                    _uiState.value = RoutesUiState.Error("No routes available yet.")
                } else {
                    _uiState.value = RoutesUiState.Success(routes)
                }
            } catch (e: Exception) {
                _uiState.value = RoutesUiState.Error(
                    e.localizedMessage ?: "Failed to load routes."
                )
            }
        }
    }

    /**
     * Проксируем поиск маршрута по id в репозиторий.
     * Удобно использовать в форме бронирования.
     */
    fun getRouteById(id: Long): Route? = routesRepository.getRouteById(id)
}
