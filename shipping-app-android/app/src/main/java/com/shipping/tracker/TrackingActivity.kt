package com.shipping.tracker

import android.os.Bundle
import android.view.View
import android.util.Log  // Add this import
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint  // Add this import
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import androidx.preference.PreferenceManager
import org.osmdroid.views.MapView
import com.shipping.tracker.databinding.ActivityTrackingBinding
import com.shipping.tracker.data.TrackingInfo
import android.content.Context
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import androidx.core.content.ContextCompat
import java.io.File

@AndroidEntryPoint
class TrackingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackingBinding
    private lateinit var map: MapView
    private val viewModel: TrackingViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configure OSMDroid
        Configuration.getInstance().apply {
            userAgentValue = "Mozilla/5.0 (Android) ShippingTracker/1.0"
            osmdroidBasePath = cacheDir
            osmdroidTileCache = File(cacheDir, "tiles")
            load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))
        }

        binding = ActivityTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    
        binding.backButton?.setOnClickListener {
            finish()
        }
    
        setupMap()
        setupObservers()
    
        // Load tracking data
        intent.getStringExtra("orderNumber")?.let { orderNumber ->
            Log.d("TrackingActivity", "Loading tracking for order: $orderNumber")
            viewModel.loadTracking(orderNumber)
        } ?: Log.e("TrackingActivity", "No order number provided")
    }

    private fun setupMap() {
        binding.mapView?.let { mapView ->
            map = mapView
            
            // Use custom tile source
            val tileSource = org.osmdroid.tileprovider.tilesource.XYTileSource(
                "OpenStreetMap",
                0, 19,
                256,
                ".png",
                arrayOf("https://tile.openstreetmap.de/"),
                "© OpenStreetMap contributors"
            )
            map.setTileSource(tileSource)
            
            map.setHorizontalMapRepetitionEnabled(false)
            map.setVerticalMapRepetitionEnabled(false)
            map.controller.setZoom(4.0)
            map.setMultiTouchControls(true)
            map.isTilesScaledToDpi = true
            map.minZoomLevel = 2.0
            map.maxZoomLevel = 18.0
            
            // Set initial bounds to show continental US
            val mapController = map.controller
            mapController.setCenter(GeoPoint(39.8283, -98.5795))
            
            // Enable hardware acceleration
            map.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        }
    }

    private fun setupObservers() {
        viewModel.trackingInfo.observe(this) { tracking ->
            Log.d("TrackingActivity", "Received tracking update: $tracking")
            tracking?.let {
                updateUI(it)
                updateMapMarkers(it)
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                binding.orderNumberText?.text = it
                binding.statusChip?.visibility = View.GONE
                binding.originText?.visibility = View.GONE
                binding.destinationText?.visibility = View.GONE
                binding.estimatedArrivalText?.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun updateUI(tracking: TrackingInfo) {
        if (tracking.orderNumber != null) {
            binding.orderNumberText?.text = getString(R.string.order_number_format).format(tracking.orderNumber)
            binding.statusChip?.text = tracking.status ?: getString(R.string.status_unknown)
            binding.originText?.text = getString(R.string.origin_format).format(tracking.origin ?: getString(R.string.location_unknown))
            binding.destinationText?.text = getString(R.string.destination_format).format(tracking.destination ?: getString(R.string.location_unknown))
            binding.estimatedArrivalText?.text = getString(R.string.eta_format).format(tracking.estimatedTimeArrival ?: getString(R.string.eta_unknown))
            
            updateMapMarkers(tracking)
        } else {
            binding.orderNumberText?.text = getString(R.string.no_tracking_data)
            binding.statusChip?.visibility = View.GONE
            binding.originText?.visibility = View.GONE
            binding.destinationText?.visibility = View.GONE
            binding.estimatedArrivalText?.visibility = View.GONE
        }
    }

    private fun updateMapMarkers(tracking: TrackingInfo) {
        binding.mapView?.let { mapView ->
            mapView.overlays.clear()
            
            // Add origin marker
            val originMarker = Marker(mapView).apply {
                position = GeoPoint(tracking.route.originLat, tracking.route.originLng)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                icon = ContextCompat.getDrawable(this@TrackingActivity, R.drawable.ic_origin)
                title = getString(R.string.marker_origin)
            }
            mapView.overlays.add(originMarker)

            // Add destination marker
            val destMarker = Marker(mapView).apply {
                position = GeoPoint(tracking.route.destLat, tracking.route.destLng)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_destination)
                title = getString(R.string.marker_destination)
            }
            mapView.overlays.add(destMarker)

            // Add current location marker
            val currentMarker = Marker(mapView).apply {
                position = GeoPoint(tracking.currentLat, tracking.currentLng)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_package)
                title = getString(R.string.marker_current)
            }
            mapView.overlays.add(currentMarker)

            // Draw route line
            val routeLine = Polyline(mapView).apply {
                addPoint(GeoPoint(tracking.route.originLat, tracking.route.originLng))
                addPoint(GeoPoint(tracking.route.destLat, tracking.route.destLng))
                outlinePaint.apply {
                    color = ContextCompat.getColor(this@TrackingActivity, R.color.route_line)
                    strokeWidth = 5f
                }
            }
            mapView.overlays.add(routeLine)

            mapView.invalidate()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView?.onResume()
    }
    
    override fun onPause() {
        super.onPause()
        binding.mapView?.onPause()
    }
}