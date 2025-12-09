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
            _uiState.value = BookingUiState(isLoading = true)

            try {
                val response = bookingRepository.createBooking(request)

                if (response.success) {
                    _uiState.value = BookingUiState(
                        isLoading = false,
                        success = true,
                        errorMessage = null,
                        createdBookingId = response.bookingId
                    )
                } else {
                    _uiState.value = BookingUiState(
                        isLoading = false,
                        success = false,
                        errorMessage = response.message ?: "Failed to create booking.",
                        createdBookingId = null
                    )
                }
            } catch (e: Exception) {
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
     */
    private fun validate(request: BookingRequest): String? {
        if (request.pickupLocation.isBlank()) return "Pickup location is required."
        if (request.dropoffLocation.isBlank()) return "Dropoff location is required."
        if (request.date.isBlank()) return "Date is required."
        // Время можем не требовать жёстко: пользователь может ввести только дату.
        if (request.passengers <= 0) return "Passengers must be greater than 0."
        if (request.name.isBlank()) return "Name is required."
        // Телефон или email: хотя бы один должен быть
        if (request.phone.isBlank() && request.email.isNullOrBlank()) {
            return "Please provide phone or email."
        }
        // routeId/routeName опциональны
        return null
    }
}
