package com.example.bdvailtransfers2.data.model

/**
 * Модель существующей брони для экрана "My Bookings".
 */
data class Booking(
    val id: Long,
    val routeName: String,
    val pickupLocation: String,
    val dropoffLocation: String,
    val date: String,
    val time: String,
    val passengers: Int,
    val status: String          // например: "pending", "confirmed", "completed", "cancelled"
)
