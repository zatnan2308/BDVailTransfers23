package com.example.bdvailtransfers2.data.network

import com.example.bdvailtransfers2.data.model.ApiResponse
import com.example.bdvailtransfers2.data.model.Booking
import com.example.bdvailtransfers2.data.model.BookingRequest
import com.example.bdvailtransfers2.data.model.BookingResponse
import com.example.bdvailtransfers2.data.model.Route
import com.example.bdvailtransfers2.data.model.SupportRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Описание REST API BDVail для приложения.
 * Здесь мы описываем только те эндпойнты, которые реально используем в приложении.
 */
interface BDVailApiService {

    /**
     * Получить список доступных маршрутов.
     *
     * Ожидаемый эндпойнт WordPress-плагина:
     * GET /wp-json/bdvail/v1/app/routes
     */
    @GET("wp-json/bdvail/v1/app/routes")
    suspend fun getRoutes(): List<Route>

    /**
     * Создать новое бронирование.
     *
     * Ожидаемый эндпойнт:
     * POST /wp-json/bdvail/v1/app/booking
     */
    @POST("wp-json/bdvail/v1/app/booking")
    suspend fun createBooking(
        @Body request: BookingRequest
    ): BookingResponse

    /**
     * Получить список бронирований по телефону.
     *
     * Ожидаемый эндпойнт:
     * GET /wp-json/bdvail/v1/app/bookings?phone=+123456789
     */
    @GET("wp-json/bdvail/v1/app/bookings")
    suspend fun getBookings(
        @Query("phone") phone: String
    ): List<Booking>

    /**
     * Отправить запрос в поддержку.
     *
     * Ожидаемый эндпойнт:
     * POST /wp-json/bdvail/v1/app/support
     */
    @POST("wp-json/bdvail/v1/app/support")
    suspend fun sendSupport(
        @Body request: SupportRequest
    ): ApiResponse
}
