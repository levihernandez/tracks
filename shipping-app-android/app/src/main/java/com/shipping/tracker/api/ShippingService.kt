package com.shipping.tracker.api

import com.shipping.tracker.data.TrackingData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ShippingService {
    @GET("api/orders/number/{id}")
    suspend fun getTracking(@Path("id") trackingId: String): Response<TrackingData>
}