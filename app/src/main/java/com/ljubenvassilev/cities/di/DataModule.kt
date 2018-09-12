package com.ljubenvassilev.cities.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.ljubenvassilev.cities.db.CitiesDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(AppModule::class))
class DataModule {

    @Singleton
    @Provides
    fun preference(context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    @Singleton
    @Provides
    fun appDatabase(context: Context): CitiesDb = Room.databaseBuilder(context,
            CitiesDb::class.java, CitiesDb.DB_NAME).build()

    @Provides
    @Singleton
    fun cityDao(citiesDb: CitiesDb) = citiesDb.cityDao()
}