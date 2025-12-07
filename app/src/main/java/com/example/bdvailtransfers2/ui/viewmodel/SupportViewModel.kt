package com.example.bdvailtransfers2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bdvailtransfers2.data.model.SupportRequest
import com.example.bdvailtransfers2.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Состояние экрана поддержки.
 */
data class SupportUiState(
    val isSending: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel для экрана "Support".
 *
 * Логика:
 * - sendSupport(request) — валидация + вызов API.
 * - uiState.success == true → UI показывает Snackbar "sent" и/или очищает форму.
 */
class SupportViewModel(
    private val bookingRepository: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SupportUiState())
    val uiState: StateFlow<SupportUiState> = _uiState.asStateFlow()

    /**
     * Сбросить состояние (например, после ухода с экрана).
     */
    fun resetState() {
        _uiState.value = SupportUiState()
    }

    /**
     * Отправка запроса в поддержку.
     */
    fun sendSupport(request: SupportRequest) {
        val validationError = validate(request)
        if (validationError != null) {
            _uiState.value = SupportUiState(
                isSending = false,
                success = false,
                errorMessage = validationError
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = SupportUiState(isSending = true)

            try {
                val response = bookingRepository.sendSupport(request)
                if (response.success) {
                    _uiState.value = SupportUiState(
                        isSending = false,
                        success = true,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = SupportUiState(
                        isSending = false,
                        success = false,
                        errorMessage = response.message ?: "Failed to send support request."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = SupportUiState(
                    isSending = false,
                    success = false,
                    errorMessage = e.localizedMessage
                        ?: "Network error while sending support request."
                )
            }
        }
    }

    /**
     * Простая валидация: имя + текст сообщения обязательны,
     * хотя бы один контакт (телефон или email) тоже нужен.
     */
    private fun validate(request: SupportRequest): String? {
        if (request.name.isBlank()) return "Name is required."
        if (request.message.isBlank()) return "Message is required."

        if (request.phone.isNullOrBlank() && request.email.isNullOrBlank()) {
            return "Please provide at least phone or email so we can contact you."
        }
        return null
    }
}
