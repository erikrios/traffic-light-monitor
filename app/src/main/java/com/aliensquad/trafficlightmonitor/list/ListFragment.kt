package com.aliensquad.trafficlightmonitor.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.core.ui.TrafficLightAdapter
import com.aliensquad.trafficlightmonitor.core.utils.Resource
import com.aliensquad.trafficlightmonitor.core.utils.Status
import com.aliensquad.trafficlightmonitor.dashboard.DashboardFragmentDirections
import com.aliensquad.trafficlightmonitor.dashboard.DashboardViewModel
import com.aliensquad.trafficlightmonitor.databinding.FragmentListBinding
import org.koin.android.viewmodel.ext.android.getViewModel


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding
    private val dashboardViewModel by lazy {
        requireParentFragment().getViewModel<DashboardViewModel>()
    }
    private val adapter =
        TrafficLightAdapter { navigateToDetailsFragment(it, recentLatitude, recentLongitude) }
    private var recentLatitude = -8.0181039
    private var recentLongitude = 111.4672751

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleRecyclerView()
        dashboardViewModel.apply {
            latitude.observe(viewLifecycleOwner) {
                recentLatitude = it
                adapter.setOnClickListener { trafficLight ->
                    navigateToDetailsFragment(trafficLight, recentLatitude, recentLongitude)
                }
            }
            longitude.observe(viewLifecycleOwner) {
                recentLongitude = it
                adapter.setOnClickListener { trafficLight ->
                    navigateToDetailsFragment(trafficLight, recentLatitude, recentLongitude)
                }
            }
            trafficLightsState.observe(
                viewLifecycleOwner,
                this@ListFragment::handleState
            )
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
        binding?.apply {
            progressBar.visibility = View.VISIBLE
            rvTrafficLights.visibility = View.INVISIBLE
        }
    }

    private fun handleErrorState(message: String) {
        binding?.progressBar?.visibility = View.GONE
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccessState(trafficLights: List<TrafficLight>?) {
        binding?.progressBar?.visibility = View.GONE
        trafficLights?.let {
            if (it.isEmpty()) {
                binding?.apply {
                    lavEmpty.visibility = View.VISIBLE
                    rvTrafficLights.visibility = View.INVISIBLE
                }
            } else {
                binding?.apply {
                    lavEmpty.visibility = View.GONE
                    rvTrafficLights.visibility = View.VISIBLE
                }
                adapter.setTrafficLights(it)
            }
        }
    }

    private fun handleRecyclerView() {
        binding?.rvTrafficLights?.adapter = adapter
    }

    private fun navigateToDetailsFragment(
        trafficLight: TrafficLight,
        latitude: Double,
        longitude: Double
    ) {
        val action =
            DashboardFragmentDirections.actionDashboardFragmentToDetailsFragment(
                trafficLight,
                latitude.toFloat(),
                longitude.toFloat()
            )
        findNavController().navigate(action)
    }
}