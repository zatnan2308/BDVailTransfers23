package com.example.bdvailtransfers2.data.model

/**
 * Модель маршрута для списка направлений / карточек.
 * Позже можно расширить полями (описание, тип авто и т.п.).
 */
data class Route(
    val id: Long,
    val name: String,
    val from: String,
    val to: String,
    val basePrice: Double,
    val currency: String,
    val durationMinutes: Int
)
