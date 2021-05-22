package com.aliensquad.trafficlightmonitor.ui.view.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper.myLooper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aliensquad.trafficlightmonitor.R
import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.databinding.FragmentDashboardBinding
import com.aliensquad.trafficlightmonitor.ui.adapter.TrafficLightAdapter
import com.aliensquad.trafficlightmonitor.ui.viewmodel.DashboardViewModel
import com.aliensquad.trafficlightmonitor.utils.PermissionUtils
import com.aliensquad.trafficlightmonitor.utils.RadiusConfiguration
import com.aliensquad.trafficlightmonitor.utils.RadiusConfiguration.generateRadiusValues
import com.aliensquad.trafficlightmonitor.utils.RadiusConfiguration.getRadiusFromIndex
import com.aliensquad.trafficlightmonitor.utils.Resource
import com.aliensquad.trafficlightmonitor.utils.Status
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                when {
                    PermissionUtils.isLocationEnabled(requireContext()) -> {
                        setUpLocationListener()
                        handleSpinner()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnableDialog(requireContext()) {
                            navigateToLocationSetting()
                        }
                    }
                }
            } else {
                Log.d(DashboardFragment::class.java.simpleName, "Permission denied")
            }
        }
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding
    private val adapter = TrafficLightAdapter { navigateToDetailsFragment(it) }
    private val viewModel: DashboardViewModel by viewModel()
    private var recentRadius = RadiusConfiguration.Radius.KM_15
    private var recentLatitude = -1.0
    private var recentLongitude = -1.0

    companion object {
        private const val RECENT_RADIUS_KEY = "recent_radius_key"
        private const val RECENT_LATITUDE_KEY = "recent_latitude_key"
        private const val RECENT_LONGITUDE_KEY = "recent_longitude_key"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            recentRadius = it.getSerializable(RECENT_RADIUS_KEY) as RadiusConfiguration.Radius
            recentLatitude = it.getDouble(RECENT_LATITUDE_KEY)
            recentLongitude = it.getDouble(RECENT_LONGITUDE_KEY)
        }
        handleToolbar()
        handleRecyclerView()
        viewModel.trafficLightsState.observe(
            viewLifecycleOwner,
            this@DashboardFragment::handleState
        )
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(requireContext()) -> {
                when {
                    PermissionUtils.isLocationEnabled(requireContext()) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnableDialog(requireContext()) {
                            navigateToLocationSetting()
                        }
                    }
                }
            }
            else -> {
                requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putSerializable(RECENT_RADIUS_KEY, recentRadius)
            putDouble(RECENT_LATITUDE_KEY, recentLatitude)
            putDouble(RECENT_LONGITUDE_KEY, recentLongitude)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleState(resource: Resource<List<TrafficLight>>) {
        when (resource.status) {
            Status.LOADING -> handleLoadingState()
            Status.ERROR -> handleErrorState(resource.message.toString())
            Status.SUCCESS -> handleSuccessState(resource.data)
        }
    }

    private fun handleLoadingState() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun handleErrorState(message: String) {
        binding?.progressBar?.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccessState(trafficLights: List<TrafficLight>?) {
        binding?.progressBar?.visibility = View.GONE
        trafficLights?.let {
            adapter.setTrafficLights(it)
        }
    }

    private fun handleRecyclerView() {
        binding?.rvTrafficLights?.adapter = adapter
    }

    private fun handleSpinner() {
        val radiusValues = generateRadiusValues()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            radiusValues
        )
        binding?.spinnerRadius?.apply {
            this.adapter = adapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d(
                        DashboardFragment::class.java.simpleName,
                        "Lat: $recentLatitude | Long: $recentLongitude"
                    )
                    val radius = getRadiusFromIndex(position)
                    if (radius != recentRadius) {
                        viewModel.getTrafficLights(radius)
                        recentRadius = radius
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun handleToolbar() {
        val menuItem = binding?.toolbar?.menu?.findItem(R.id.item_about_apps)
        menuItem?.setOnMenuItemClickListener {
            navigateToAboutFragment()
            return@setOnMenuItemClickListener true
        }
    }

    private fun navigateToDetailsFragment(trafficLight: TrafficLight) {
        val action =
            DashboardFragmentDirections.actionDashboardFragmentToDetailsFragment(trafficLight)
        findNavController().navigate(action)
    }

    private fun navigateToAboutFragment() {
        val action = DashboardFragmentDirections.actionDashboardFragmentToAboutFragment()
        findNavController().navigate(action)
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        val locationRequest = LocationRequest.create().apply {
            interval = 200L
            fastestInterval = 200L
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            recentLatitude = location.latitude
                            recentLongitude = location.longitude
                        }
                    }
                },
                myLooper()
            )
        }
    }

    private fun navigateToLocationSetting() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context?.startActivity(intent)
    }

    private fun handlePermissionGrantedView() {
        binding?.apply {
            lavPermissionDenied.visibility = View.GONE
            btnRequestPermission.visibility = View.GONE
            spinnerRadius.visibility = View.VISIBLE
            rvTrafficLights.visibility = View.VISIBLE
            tvRadius.visibility = View.VISIBLE
        }
    }

    private fun handlePermissionDeniedView() {
        binding?.apply {
            lavPermissionDenied.visibility = View.VISIBLE
            btnRequestPermission.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
            spinnerRadius.visibility = View.GONE
            rvTrafficLights.visibility = View.GONE
            tvRadius.visibility = View.GONE
        }
    }
}