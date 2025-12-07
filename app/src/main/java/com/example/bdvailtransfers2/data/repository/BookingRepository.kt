package com.example.bdvailtransfers2.data.repository

import com.example.bdvailtransfers2.data.model.Booking
import com.example.bdvailtransfers2.data.model.BookingRequest
import com.example.bdvailtransfers2.data.model.BookingResponse
import com.example.bdvailtransfers2.data.model.ApiResponse
import com.example.bdvailtransfers2.data.model.SupportRequest
import com.example.bdvailtransfers2.data.network.BDVailApiService
import com.example.bdvailtransfers2.data.network.NetworkModule

/**
 * Репозиторий для работы с бронированиями и поддержкой.
 */
class BookingRepository(
    private val api: BDVailApiService = NetworkModule.apiService
) {

    /**
     * Создать новую бронь.
     */
    suspend fun createBooking(request: BookingRequest): BookingResponse {
        return api.createBooking(request)
    }

    /**
     * Получить список бронирований по телефону.
     */
    suspend fun getBookings(phone: String): List<Booking> {
        return api.getBookings(phone)
    }

    /**
     * Отправить запрос в поддержку.
     */
    suspend fun sendSupport(request: SupportRequest): ApiResponse {
        return api.sendSupport(request)
    }
}
