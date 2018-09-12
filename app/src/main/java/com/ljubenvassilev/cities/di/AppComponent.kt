package com.ljubenvassilev.cities.di

import com.ljubenvassilev.cities.cities.viewmodel.CitiesViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(into: CitiesViewModel)
}
