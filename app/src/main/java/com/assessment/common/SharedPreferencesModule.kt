package com.assessment.common

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: FragmentActivity?): SharedPreferences? {
        return context?.getSharedPreferences("Assessment", Context.MODE_PRIVATE)
    }
}