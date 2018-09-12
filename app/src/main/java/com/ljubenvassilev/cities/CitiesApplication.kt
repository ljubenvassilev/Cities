package com.ljubenvassilev.cities

import android.app.Application
import com.ljubenvassilev.cities.di.AppComponent
import com.ljubenvassilev.cities.di.AppModule
import com.ljubenvassilev.cities.di.DaggerAppComponent

class CitiesApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}