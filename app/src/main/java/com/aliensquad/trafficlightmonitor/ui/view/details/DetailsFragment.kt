package com.aliensquad.trafficlightmonitor.ui.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aliensquad.trafficlightmonitor.R
import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.databinding.FragmentDetailsBinding
import com.aliensquad.trafficlightmonitor.ui.adapter.IntersectionAdapter
import com.aliensquad.trafficlightmonitor.utils.DummyData.getTrafficLight
import com.aliensquad.trafficlightmonitor.utils.ImageConfiguration.getRandomTrafficLightWallpaperResource
import com.bumptech.glide.Glide

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding
    private val args: DetailsFragmentArgs by navArgs()
    private val trafficLight = getTrafficLight(args.trafficLight.id)
    private val adapter = IntersectionAdapter()

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
        trafficLight?.let { handleView(it) }
        adapter.setTrafficLights(trafficLight?.intersections ?: listOf())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleView(trafficLight: TrafficLight) {
        val trafficLightImageResource = getRandomTrafficLightWallpaperResource()

        binding?.apply {
            Glide.with(requireContext())
                .load(trafficLightImageResource)
                .into(imgTrafficLight)
            tvName.text = trafficLight.name
            tvAddress.text = trafficLight.address
            tvVehicleDensityInMinutes.text =
                getString(R.string.vehicles_per_minutes, trafficLight.vehiclesDensityInMinutes)
        }
        handleRecyclerView()
    }

    private fun handleToolbar(title: String) {
        binding?.toolbar?.apply {
            this.title = title
            navigationIcon =
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun handleRecyclerView() {
        binding?.rvRecyclerViewIntersections?.adapter = adapter
    }
}