package com.example.bdvailtransfers2.data.model

/**
 * Данные, которые приложение отправляет в API при создании брони.
 * Поля подогнаны под типичную форму BDVail (pickup, dropoff, дата/время, пассажиры, контакты).
 */
data class BookingRequest(
    val routeId: Long?,
    val routeName: String?,
    val pickupLocation: String,
    val dropoffLocation: String,
    val date: String,          // формата "2025-12-06"
    val time: String,          // формата "14:30"
    val passengers: Int,
    val childSeats: Int,
    val luggage: Int,
    val flightNumber: String?, // если есть
    val name: String,
    val phone: String,
    val email: String?,
    val comment: String?
)
