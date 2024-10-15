package com.assessment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.assessment.common.SharedPreferencesModule
import com.assessment.viewmodel.WeatherViewModel
import com.assignment.myapplication.databinding.WeatherDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class WeatherReportFragment : Fragment() {

    private lateinit var binding: WeatherDetailsBinding

    val viewModel: WeatherViewModel by viewModels()

    val preferences = context?.getSharedPreferences("Assessment", Context.MODE_PRIVATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WeatherDetailsBinding.inflate(
            LayoutInflater.from(context), container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.weatherViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.countryName.setText(preferences?.getString("country", ""))
        binding.cityName.setText(preferences?.getString("city", ""))
        binding.stateName.setText(preferences?.getString("state", ""))
        binding.submit.setOnClickListener {
            commitSearch()
            performSearch()
        }
        performSearch()
    }

    private fun performSearch() {
        viewModel.getWeatherDetails(
            binding.cityName.text.toString(),
            binding.stateName.text.toString(),
            binding.countryName.text.toString()
        )
    }

    private fun commitSearch() {
        val editor = preferences?.edit()
        editor?.putString("city", binding.cityName.text.toString())
        editor?.putString("state", binding.stateName.text.toString())
        editor?.putString("country", binding.countryName.text.toString())
        val x = editor?.apply()
    }
}
