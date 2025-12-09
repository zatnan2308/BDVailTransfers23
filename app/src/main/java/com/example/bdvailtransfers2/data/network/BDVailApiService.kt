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
 * REST API интерфейс для сайта BDVail.
 *
 * Пути должны совпадать с эндпойнтами WordPress-плагина:
 *  - GET  /wp-json/bdvail/v1/app/routes
 *  - POST /wp-json/bdvail/v1/app/booking
 *  - GET  /wp-json/bdvail/v1/app/bookings?phone=...
 *  - POST /wp-json/bdvail/v1/app/support
 */
interface BDVailApiService {

    /**
     * Список маршрутов для экрана Routes.
     */
    @GET("wp-json/bdvail/v1/app/routes")
    suspend fun getRoutes(): List<Route>

    /**
     * Создание нового бронирования.
     */
    @POST("wp-json/bdvail/v1/app/booking")
    suspend fun createBooking(
        @Body request: BookingRequest
    ): BookingResponse

    /**
     * Список уже созданных бронирований по номеру телефона.
     */
    @GET("wp-json/bdvail/v1/app/bookings")
    suspend fun getBookings(
        @Query("phone") phone: String
    ): List<Booking>

    /**
     * Отправка запроса в поддержку.
     */
    @POST("wp-json/bdvail/v1/app/support")
    suspend fun sendSupport(
        @Body request: SupportRequest
    ): ApiResponse
}
