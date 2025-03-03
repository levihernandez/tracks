package com.shipping.tracker.data

data class TrackingData(
    val id: Int,
    val orderNumber: String,
    val origin: String,
    val destination: String,
    val status: String,
    val cost: Double,
    val routeId: Int,
    val currentLat: Double,
    val currentLng: Double,
    val estimatedTimeArrival: String,
    val createdAt: String,
    val updatedAt: String
) {
    val route: RouteInfo
        get() = RouteInfo(
            originLat = 40.7128,
            originLng = -74.0060,
            destLat = 34.0522,
            destLng = -118.2437
        )
}

data class RouteInfo(
    val originLat: Double,
    val originLng: Double,
    val destLat: Double,
    val destLng: Double
)

typealias TrackingInfo = TrackingData