package com.example.bdvailtransfers2.data.model

/**
 * Универсальный простой ответ API (успех + сообщение).
 */
data class ApiResponse(
    val success: Boolean,
    val message: String?
)
