package com.aliensquad.trafficlightmonitor.ui.view.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aliensquad.trafficlightmonitor.R
import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.databinding.FragmentDashboardBinding
import com.aliensquad.trafficlightmonitor.ui.adapter.TrafficLightAdapter
import com.aliensquad.trafficlightmonitor.ui.viewmodel.DashboardViewModel
import com.aliensquad.trafficlightmonitor.utils.RadiusConfiguration.generateRadiusValues
import com.aliensquad.trafficlightmonitor.utils.RadiusConfiguration.getRadiusFromIndex
import com.aliensquad.trafficlightmonitor.utils.Resource
import com.aliensquad.trafficlightmonitor.utils.Status
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding
    private val adapter = TrafficLightAdapter { navigateToDetailsFragment(it) }
    private val viewModel: DashboardViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleToolbar()
        handleRecyclerView()
        handleSpinner()
        viewModel.trafficLightsState.observe(
            viewLifecycleOwner,
            this@DashboardFragment::handleState
        )
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
                    val radius = getRadiusFromIndex(position)
                    viewModel.getTrafficLights(radius)
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
}