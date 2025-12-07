package com.example.bdvailtransfers2.data.model

/**
 * Запрос в поддержку (экран Support).
 */
data class SupportRequest(
    val name: String,
    val phone: String?,
    val email: String?,
    val subject: String,
    val message: String
)
