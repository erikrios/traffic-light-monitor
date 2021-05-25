package com.aliensquad.trafficlightmonitor.route

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.aliensquad.trafficlightmonitor.BuildConfig
import com.aliensquad.trafficlightmonitor.R
import com.aliensquad.trafficlightmonitor.core.data.model.Intersection
import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.databinding.FragmentRouteBinding
import com.google.gson.Gson
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteFragment : Fragment() {

    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding
    private val args: RouteFragmentArgs by navArgs()
    private lateinit var mapboxMap: MapboxMap
    private lateinit var locationComponent: LocationComponent
    private lateinit var myLocation: LatLng
    private lateinit var permissionManager: PermissionsManager
    private lateinit var navigationMapRoute: NavigationMapRoute
    private var currentRoute: DirectionsRoute? = null

    companion object {
        private const val ICON_ID = "ICON_ID"
        private const val SECONDARY_ICON_ID = "SECONDARY_ICON_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(requireContext().applicationContext, BuildConfig.PUBLIC_TOKEN)
        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.mapView?.apply {
            onCreate(savedInstanceState)
            getMapAsync { mapboxMap ->
                this@RouteFragment.mapboxMap = mapboxMap
                showMarker(args.trafficLight)
            }
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.mapView?.onSaveInstanceState(outState)
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

    private fun showMarker(trafficLight: TrafficLight) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            style.addImage(
                ICON_ID,
                BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_default)
            )

            val symbolManager = binding?.mapView?.let { SymbolManager(it, mapboxMap, style) }
            symbolManager?.iconAllowOverlap = true

            symbolManager?.create(
                SymbolOptions()
                    .withTextField(trafficLight.name)
                    .withLatLng(LatLng(trafficLight.latitude, trafficLight.longitude))
                    .withIconImage(ICON_ID)
                    .withIconSize(0.8f)
                    .withTextAnchor("top")
                    .withTextSize(12f)
                    .withData(Gson().toJsonTree(trafficLight))
            )

            trafficLight.intersections?.let { showIntersectionMarker(style, it) }

            mapboxMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        trafficLight.latitude,
                        trafficLight.longitude
                    ), 8.0
                )
            )

            navigationMapRoute = NavigationMapRoute(
                null,
                binding?.mapView as MapView,
                mapboxMap,
                R.style.NavigationMapRoute
            )
            showMyLocation(style)
            val destination = Point.fromLngLat(trafficLight.longitude, trafficLight.latitude)
            val origin =
                Point.fromLngLat(args.userLongitude.toDouble(), args.userLatitude.toDouble())
            requestRoute(origin, destination)
        }
    }

    private fun showIntersectionMarker(style: Style, intersections: List<Intersection>) {
        style.addImage(
            SECONDARY_ICON_ID,
            BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_default)
        )
        val latLngBoundsBuilder = LatLngBounds.Builder()

        val symbolManager = binding?.mapView?.let { SymbolManager(it, mapboxMap, style) }
        symbolManager?.iconAllowOverlap = true

        val options = mutableListOf<SymbolOptions>()
        intersections.forEach { intersection ->
            latLngBoundsBuilder.include(LatLng(intersection.latitude, intersection.longitude))
            options.add(
                SymbolOptions()
                    .withTextField(intersection.name)
                    .withLatLng(LatLng(intersection.latitude, intersection.longitude))
                    .withIconImage(SECONDARY_ICON_ID)
                    .withIconColor("green")
                    .withIconSize(0.5f)
                    .withTextAnchor("top")
                    .withTextSize(8f)
                    .withData(Gson().toJsonTree(intersection))
            )
        }
        symbolManager?.create(options)

        latLngBoundsBuilder.build()
    }

    @SuppressLint("MissingPermission")
    private fun showMyLocation(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            val locationComponentOptions = LocationComponentOptions.builder(requireContext())
                .pulseEnabled(true)
                .pulseColor(Color.BLUE)
                .pulseAlpha(.4f)
                .pulseInterpolator(BounceInterpolator())
                .build()
            val locationComponentActivationOptions = LocationComponentActivationOptions
                .builder(requireContext(), style)
                .locationComponentOptions(locationComponentOptions)
                .build()
            locationComponent = mapboxMap.locationComponent
            locationComponent.apply {
                activateLocationComponent(locationComponentActivationOptions)
                isLocationComponentEnabled = true
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.COMPASS
            }

            var latitude: Double
            var longitude: Double
            try {
                latitude = locationComponent.lastKnownLocation?.latitude as Double
                longitude = locationComponent.lastKnownLocation?.longitude as Double
            } catch (e: Exception) {
                latitude = args.userLatitude.toDouble()
                longitude = args.userLongitude.toDouble()
            }

            myLocation = LatLng(latitude, longitude)
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12.0))
        } else {
            permissionManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                    Toast.makeText(
                        context,
                        getString(R.string.enable_location_permission_message),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mapboxMap.getStyle { style ->
                            showMyLocation(style)
                        }
                    } else {
                        requireActivity().finish()
                    }
                }
            })
            permissionManager.requestLocationPermissions(requireActivity())
        }
    }

    private fun requestRoute(origin: Point, destination: Point) {
        NavigationRoute.builder(requireContext())
            .accessToken(BuildConfig.PUBLIC_TOKEN)
            .origin(origin)
            .destination(destination)
            .build()
            .getRoute(object : Callback<DirectionsResponse> {
                override fun onResponse(
                    call: Call<DirectionsResponse>,
                    response: Response<DirectionsResponse>
                ) {
                    if (response.body() == null || response.body()?.routes()?.size == 0) {
                        Toast.makeText(
                            context,
                            getString(R.string.route_not_found_message),
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    currentRoute = response.body()?.routes()?.get(0)
                    navigationMapRoute.addRoute(currentRoute)
                    showNavigation()
                }

                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Error: $t",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showNavigation() {
        binding?.fabNavigation?.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                val simulateRoute = true

                val options = NavigationLauncherOptions.builder()
                    .directionsRoute(currentRoute)
                    .shouldSimulateRoute(simulateRoute)
                    .build()

                NavigationLauncher.startNavigation(requireActivity(), options)
            }
        }
    }
}