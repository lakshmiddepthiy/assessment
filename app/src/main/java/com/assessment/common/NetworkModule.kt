package com.assessment.common

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import com.google.gson.GsonBuilder
import com.assessment.service.WeatherDataService
import com.assessment.repo.WeatherRepository
import com.assessment.repo.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideCountriesService(): WeatherDataService {
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.BASE_URL)
            .build()
        return retrofit.create(WeatherDataService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiHelper(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository = weatherRepositoryImpl

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: FragmentActivity?): SharedPreferences? {
        return context?.getSharedPreferences("Assessment", Context.MODE_PRIVATE)
    }

}
