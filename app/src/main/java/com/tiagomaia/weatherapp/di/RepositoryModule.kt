package com.tiagomaia.weatherapp.di

import com.tiagomaia.weatherapp.network.ApiInterface
import com.tiagomaia.weatherapp.network.OWMRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
        @Singleton
        @Provides
        fun provideWeatherRepository(
            apiInterface: ApiInterface
        ): OWMRepo {
            return OWMRepo(apiInterface)
        }
}