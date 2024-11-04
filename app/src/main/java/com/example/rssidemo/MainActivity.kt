package com.example.rssidemo

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Usage in an Activity
class MainActivity : AppCompatActivity() {
    private lateinit var wifiRssiScanner: WifiRssiScanner
    private lateinit var accessPointAdapter: AccessPointAdapter
    private var isScanning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup RecyclerView
        val rvAccessPoints: RecyclerView = findViewById(R.id.rvAccessPoints)
        accessPointAdapter = AccessPointAdapter()
        rvAccessPoints.adapter = accessPointAdapter
        rvAccessPoints.layoutManager = LinearLayoutManager(this)

        // Setup Start/Stop Button
        val btnStartStop: Button = findViewById(R.id.btnStartStop)
        btnStartStop.setOnClickListener {
            toggleScanning()
        }

        // Initialize WiFi Scanner with callback
        wifiRssiScanner =
            WifiRssiScanner(this) { accessPointInfo ->
                runOnUiThread {
                    accessPointAdapter.updateAccessPoint(accessPointInfo)
                }
            }
    }

    private fun toggleScanning() {
        val btnStartStop: Button = findViewById(R.id.btnStartStop)

        if (!isScanning) {
            wifiRssiScanner.startRssiScanning()
            btnStartStop.text = "Stop Scanning"
            isScanning = true
        } else {
            wifiRssiScanner.stopRssiScanning()
            btnStartStop.text = "Start Scanning"
            isScanning = false
        }
    }

    override fun onResume() {
        super.onResume()
        wifiRssiScanner.startRssiScanning()
    }

    override fun onPause() {
        super.onPause()
        wifiRssiScanner.stopRssiScanning()
    }

    // Handle permission request results
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == WifiRssiScanner.PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                wifiRssiScanner.startRssiScanning()
            } else {
                // Handle permission denial
                Toast
                    .makeText(
                        this,
                        "WiFi scanning permissions are required",
                        Toast.LENGTH_SHORT,
                    ).show()
            }
        }
    }
}
