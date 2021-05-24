package com.aliensquad.trafficlightmonitor.ui.view.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aliensquad.trafficlightmonitor.databinding.FragmentMapsBinding
import com.aliensquad.trafficlightmonitor.ui.viewmodel.DashboardViewModel
import org.koin.android.viewmodel.ext.android.getViewModel

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding
    private val dashboardViewModel by lazy {
        requireParentFragment().getViewModel<DashboardViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding?.root
    }
}