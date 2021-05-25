package com.aliensquad.trafficlightmonitor.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aliensquad.trafficlightmonitor.R
import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.core.ui.IntersectionAdapter
import com.aliensquad.trafficlightmonitor.core.utils.ImageConfiguration.getRandomTrafficLightWallpaperResource
import com.aliensquad.trafficlightmonitor.core.utils.Resource
import com.aliensquad.trafficlightmonitor.core.utils.Status
import com.aliensquad.trafficlightmonitor.databinding.FragmentDetailsBinding
import com.bumptech.glide.Glide
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding
    private val args: DetailsFragmentArgs by navArgs()
    private val adapter = IntersectionAdapter()
    private val viewModel: DetailsViewModel by viewModel()
    private var userLatitude: Double = -1.0
    private var userLongitude: Double = -1.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleToolbar(args.trafficLight.name)
        userLatitude = args.userLatitude.toDouble()
        userLongitude = args.userLongitude.toDouble()
        adapter.setTrafficLights(listOf())
        handleRecyclerView()
        val trafficLightImageResource = getRandomTrafficLightWallpaperResource()
        loadTrafficLightImage(trafficLightImageResource)
        viewModel.apply {
            getTrafficLight(args.trafficLight.id)
            trafficLightState.observe(viewLifecycleOwner, this@DetailsFragment::handleState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleState(resource: Resource<TrafficLight>) {
        when (resource.status) {
            Status.LOADING -> handleLoadingState()
            Status.ERROR -> handleErrorState(resource.message)
            Status.SUCCESS -> handleSuccessState(resource.data)
        }
    }

    private fun handleLoadingState() {
        binding?.apply {
            tvName.text = args.trafficLight.name
            tvAddress.text = args.trafficLight.address
            tvVehicleDensityInMinutes.text =
                getString(R.string.vehicles_per_minutes, 0)
        }
    }

    private fun handleErrorState(message: String?) {
        binding?.apply {
            tvName.text = args.trafficLight.name
            tvAddress.text = args.trafficLight.address
            tvVehicleDensityInMinutes.text =
                getString(R.string.vehicles_per_minutes, 0)
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccessState(trafficLight: TrafficLight?) {
        handleView(trafficLight)
    }

    private fun handleView(trafficLight: TrafficLight?) {
        binding?.apply {
            tvName.text = trafficLight?.name
            tvAddress.text = trafficLight?.address
            tvVehicleDensityInMinutes.text =
                getString(R.string.vehicles_per_minutes, trafficLight?.vehiclesDensityInMinutes)
        }
        adapter.setTrafficLights(trafficLight?.intersections ?: listOf())
    }

    private fun handleToolbar(title: String) {
        binding?.toolbar?.apply {
            this.title = title
            navigationIcon =
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener { findNavController().popBackStack() }
            val menuItem = menu.findItem(R.id.item_about_apps)
            menuItem.setOnMenuItemClickListener {
                navigateToAboutFragment()
                return@setOnMenuItemClickListener true
            }
        }
    }

    private fun handleRecyclerView() {
        binding?.rvRecyclerViewIntersections?.adapter = adapter
    }

    private fun loadTrafficLightImage(resource: Int) {
        binding?.imgTrafficLight?.let {
            Glide.with(requireContext())
                .load(resource)
                .into(it)
        }
    }

    private fun navigateToAboutFragment() {
        val action = DetailsFragmentDirections.actionDetailsFragmentToAboutFragment()
        findNavController().navigate(action)
    }
}