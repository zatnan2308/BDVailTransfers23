package com.example.bdvailtransfers2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bdvailtransfers2.data.model.Booking
import com.example.bdvailtransfers2.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Состояние экрана "My bookings".
 */
data class MyBookingsUiState(
    val isLoading: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val errorMessage: String? = null,
    val lastPhone: String? = null
)

/**
 * ViewModel для экрана "My trips / My bookings".
 *
 * Логика:
 * - loadBookings(phone) — дергаем API и обновляем uiState.
 * - refresh() — повторная загрузка по последнему телефону.
 */
class MyBookingsViewModel(
    private val bookingRepository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyBookingsUiState())
    val uiState: StateFlow<MyBookingsUiState> = _uiState.asStateFlow()

    /**
     * Загрузить список бронирований по телефону.
     */
    fun loadBookings(phone: String) {
        if (phone.isBlank()) {
            _uiState.value = MyBookingsUiState(
                isLoading = false,
                bookings = emptyList(),
                errorMessage = "Phone is required to fetch bookings.",
                lastPhone = null
            )
            return
        }

        viewModelScope.launch {
            // Ставим флаг загрузки и запоминаем телефон
            _uiState.value = MyBookingsUiState(
                isLoading = true,
                bookings = emptyList(),
                errorMessage = null,
                lastPhone = phone
            )

            try {
                val bookings = bookingRepository.getBookings(phone)
                _uiState.value = MyBookingsUiState(
                    isLoading = false,
                    bookings = bookings,
                    errorMessage = null,
                    lastPhone = phone
                )
            } catch (e: Exception) {
                _uiState.value = MyBookingsUiState(
                    isLoading = false,
                    bookings = emptyList(),
                    errorMessage = e.localizedMessage ?: "Failed to load bookings.",
                    lastPhone = phone
                )
            }
        }
    }

    /**
     * Повторная загрузка по последнему использованному телефону.
     * Удобно привязать к "pull to refresh" или кнопке "Refresh".
     */
    fun refresh() {
        val phone = _uiState.value.lastPhone
        if (!phone.isNullOrBlank()) {
            loadBookings(phone)
        }
    }
}
