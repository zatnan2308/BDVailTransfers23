package com.example.bdvailtransfers2.data.model

/**
 * Ответ API на создание бронирования.
 */
data class BookingResponse(
    val success: Boolean,
    val bookingId: Long?,
    val message: String?
)
