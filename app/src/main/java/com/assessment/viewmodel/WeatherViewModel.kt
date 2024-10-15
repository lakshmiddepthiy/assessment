package com.assessment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assessment.common.Constants
import com.assessment.model.WeatherResponse
import com.assessment.repo.ResponseWrapper
import com.assessment.repo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val mainRepository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _weatherListLiveData: MutableLiveData<WeatherResponse> = MutableLiveData()
    var weatherListLiveData: LiveData<WeatherResponse> = _weatherListLiveData

    private val _errorMessageLiveData: MutableLiveData<String> = MutableLiveData(Constants.LOADING)
    var errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    private val _progressBarLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    var progressBarLiveData: LiveData<Boolean> = _progressBarLiveData

    fun getWeatherDetails(city: String, state: String, countryCode: String) {
        _progressBarLiveData.value = true
        if (city.isBlank() && state.isBlank() && countryCode.isBlank()) {
            _progressBarLiveData.value = false
            return
        }
        viewModelScope.launch(dispatcher) {
            when (val countriesResponse =
                mainRepository.getWeatherService(prepareRequest(city, state, countryCode))) {
                is ResponseWrapper.Success -> {
                    _progressBarLiveData.postValue(false)
                    countriesResponse.data?.let {
                        _errorMessageLiveData.postValue(String())
                        _weatherListLiveData.postValue(it)
                    } ?: run {
                        _errorMessageLiveData.postValue(Constants.EMPTY_DATA)
                    }
                }

                is ResponseWrapper.Error -> {
                        _progressBarLiveData.postValue(false)
                    _errorMessageLiveData.postValue(countriesResponse.errorWrapper.errorMessage)
                }
            }
        }
    }

    fun prepareRequest(city: String, state: String, countryCode: String): String {
        var query = StringBuilder()
        query.append(append(city))
        query.append(append(state))
        query.append(append(countryCode))
        return query.toString();
    }

    fun append(keyword: String?): String {
        return if (keyword.isNullOrBlank()) keyword ?: ""
        else "$keyword,"
    }
}