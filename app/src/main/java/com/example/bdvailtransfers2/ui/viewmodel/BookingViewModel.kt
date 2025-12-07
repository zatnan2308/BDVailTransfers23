package com.example.bdvailtransfers2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bdvailtransfers2.data.model.BookingRequest
import com.example.bdvailtransfers2.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Состояние процесса создания бронирования.
 */
data class BookingUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val createdBookingId: Long? = null
)

/**
 * ViewModel для экрана формы бронирования.
 *
 * Логика:
 * 1) Внешний код собирает BookingRequest из полей формы.
 * 2) submitBooking() валидирует request.
 * 3) При успехе вызывает API через BookingRepository.
 * 4) В uiState попадает результат (success / error + bookingId).
 * 5) UI реагирует: навигация на Success / показ Snackbar с ошибкой.
 */
class BookingViewModel(
    private val bookingRepository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    /**
     * Сбросить состояние в дефолтное.
     * Удобно вызывать после ухода с экрана успеха.
     */
    fun resetState() {
        _uiState.value = BookingUiState()
    }

    /**
     * Отправка запроса на создание бронирования.
     */
    fun submitBooking(request: BookingRequest) {
        val validationError = validate(request)
        if (validationError != null) {
            _uiState.value = BookingUiState(
                isLoading = false,
                success = false,
                errorMessage = validationError,
                createdBookingId = null
            )
            return
        }

        viewModelScope.launch {
            // Стартуем загрузку
            _uiState.value = BookingUiState(isLoading = true)

            try {
                val response = bookingRepository.createBooking(request)

                if (response.success) {
                    // Успех — можно навигироваться на экран Success
                    _uiState.value = BookingUiState(
                        isLoading = false,
                        success = true,
                        errorMessage = null,
                        createdBookingId = response.bookingId
                    )
                } else {
                    // Сервер вернул ошибку
                    _uiState.value = BookingUiState(
                        isLoading = false,
                        success = false,
                        errorMessage = response.message ?: "Failed to create booking.",
                        createdBookingId = null
                    )
                }
            } catch (e: Exception) {
                // Ошибка сети / парсинга
                _uiState.value = BookingUiState(
                    isLoading = false,
                    success = false,
                    errorMessage = e.localizedMessage
                        ?: "Network error while creating booking.",
                    createdBookingId = null
                )
            }
        }
    }

    /**
     * Базовая валидация обязательных полей.
     * Тут можно будет легко расширять под реальные требования формы.
     */
    private fun validate(request: BookingRequest): String? {
        if (request.pickupLocation.isBlank()) return "Pickup location is required."
        if (request.dropoffLocation.isBlank()) return "Dropoff location is required."
        if (request.date.isBlank()) return "Date is required."
        if (request.time.isBlank()) return "Time is required."
        if (request.passengers <= 0) return "Passengers must be greater than 0."
        if (request.name.isBlank()) return "Name is required."
        if (request.phone.isBlank()) return "Phone is required."
        // routeId/routeName опциональны: иногда маршрут может быть кастомным
        return null
    }
}
