package com.aliensquad.trafficlightmonitor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aliensquad.trafficlightmonitor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_TrafficLightMonitor_NoActionBar)
        setContentView(binding.root)
    }
}