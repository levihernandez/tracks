package com.shipping.tracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shipping.tracker.databinding.ActivitySettingsBinding
import com.shipping.tracker.api.ApiClient

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("ApiSettings", MODE_PRIVATE)

        // Load current value using BuildConfig default
        binding.serverUrlInput.setText(prefs.getString("server_url", BuildConfig.API_BASE_URL))

        binding.saveButton.setOnClickListener {
            prefs.edit()
                .putString("server_url", binding.serverUrlInput.text.toString())
                .apply()
            ApiClient.updateBaseUrl(binding.serverUrlInput.text.toString())
            finish()
        }
    }
}