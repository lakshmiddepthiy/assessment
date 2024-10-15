package com.assessment.service

import com.assessment.common.Constants
import com.assessment.model.WeatherResponse
import com.assignment.myapplication.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherDataService {

    @GET(Constants.WEATHER_API_PATH)
    suspend fun getWeatherReport(
        @Query("q") query: String,
        @Query("appid") appid: String = BuildConfig.API_KEY
    ): Response<WeatherResponse>
}