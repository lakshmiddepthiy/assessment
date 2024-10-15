package com.assessment.repo

import com.assessment.service.WeatherDataService
import com.assessment.model.WeatherResponse
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getWeatherService(keyWord: String): ResponseWrapper<WeatherResponse>
}

class WeatherRepositoryImpl @Inject constructor(private val weatherDataService: WeatherDataService) :
    BaseRepository(),
    WeatherRepository {
    override suspend fun getWeatherService(keyWord: String): ResponseWrapper<WeatherResponse> {
        return submitRestApiCall(
            call = { weatherDataService.getWeatherReport(keyWord) },
            errorClass = ErrorResponse::class
        )
    }
}