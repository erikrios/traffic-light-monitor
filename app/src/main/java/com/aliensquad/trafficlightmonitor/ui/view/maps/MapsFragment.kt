package com.aliensquad.trafficlightmonitor.ui.view.maps

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aliensquad.trafficlightmonitor.BuildConfig.PUBLIC_TOKEN
import com.aliensquad.trafficlightmonitor.R
import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.databinding.FragmentMapsBinding
import com.aliensquad.trafficlightmonitor.ui.view.dashboard.DashboardFragmentDirections
import com.aliensquad.trafficlightmonitor.ui.viewmodel.DashboardViewModel
import com.aliensquad.trafficlightmonitor.utils.Resource
import com.google.gson.Gson
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import org.koin.android.viewmodel.ext.android.getViewModel

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding
    private val dashboardViewModel by lazy {
        requireParentFragment().getViewModel<DashboardViewModel>()
    }
    private lateinit var mapboxMap: MapboxMap

    companion object {
        private const val ICON_ID = "ICON_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(requireContext(), PUBLIC_TOKEN)
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.mapView?.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            getTrafficLightsData()
        }
    }

    private fun getTrafficLightsData() {
        dashboardViewModel.trafficLightsState.observe(
            viewLifecycleOwner,
            this@MapsFragment::handleState
        )
    }

    private fun handleState(resource: Resource<List<TrafficLight>>) {
        showMarker(resource.data ?: listOf())
    }

    override fun onStart() {
        super.onStart()
        binding?.mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding?.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding?.mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding?.mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapView?.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.mapView?.onSaveInstanceState(outState)
    }

    private fun showMarker(trafficLights: List<TrafficLight>) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            style.addImage(
                ICON_ID,
                BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_default)
            )
            val latLngBoundsBuilder = LatLngBounds.Builder()

            val symbolManager = binding?.mapView?.let { SymbolManager(it, mapboxMap, style) }
            symbolManager?.iconAllowOverlap = true

            val options = mutableListOf<SymbolOptions>()
            trafficLights.forEach { trafficLight ->
                latLngBoundsBuilder.include(LatLng(trafficLight.latitude, trafficLight.longitude))
                options.add(
                    SymbolOptions()
                        .withTextField(trafficLight.name)
                        .withLatLng(LatLng(trafficLight.latitude, trafficLight.longitude))
                        .withIconImage(ICON_ID)
                        .withIconSize(0.8f)
                        .withTextAnchor("top")
                        .withTextSize(12f)
                        .withData(Gson().toJsonTree(trafficLight))
                )
            }
            symbolManager?.create(options)

            val latLngBounds = latLngBoundsBuilder.build()
            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 5000)

            symbolManager?.addClickListener { symbol ->
                val data = Gson().fromJson(symbol.data, TrafficLight::class.java)
                navigateToDetailsFragment(data)
            }
        }
    }

    private fun navigateToDetailsFragment(trafficLight: TrafficLight) {
        val action =
            DashboardFragmentDirections.actionDashboardFragmentToDetailsFragment(trafficLight)
        findNavController().navigate(action)
    }
}