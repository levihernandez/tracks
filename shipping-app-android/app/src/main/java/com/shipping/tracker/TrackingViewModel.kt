package com.shipping.tracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.shipping.tracker.data.TrackingInfo
import android.util.Log
import retrofit2.HttpException
import com.shipping.tracker.api.ShippingService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val shippingService: ShippingService
) : ViewModel() {
    
    private val _trackingInfo = MutableLiveData<TrackingInfo?>()
    val trackingInfo: LiveData<TrackingInfo?> = _trackingInfo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadTracking(orderNumber: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                Log.d("TrackingViewModel", "Fetching tracking for order: $orderNumber")
                val response = shippingService.getTracking(orderNumber)
                
                if (response.isSuccessful && response.body()?.orderNumber != null) {
                    Log.d("TrackingViewModel", "Success: ${response.body()}")
                    _trackingInfo.value = response.body()
                } else {
                    val errorMsg = when (response.code()) {
                        404 -> "Order not found"
                        500 -> "Server error. Please try again later."
                        else -> "Error loading tracking information"
                    }
                    Log.e("TrackingViewModel", "Error ${response.code()}: ${response.errorBody()?.string()}")
                    _error.value = errorMsg
                    _trackingInfo.value = null
                }
            } catch (e: Exception) {
                Log.e("TrackingViewModel", "Exception: ${e.message}", e)
                _error.value = "Network error. Please check your connection."
                _trackingInfo.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}