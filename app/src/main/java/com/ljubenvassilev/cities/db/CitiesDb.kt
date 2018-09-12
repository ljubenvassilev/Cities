package com.ljubenvassilev.cities.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ljubenvassilev.cities.cities.entities.CityEntity

@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class CitiesDb : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        const val DB_NAME = "cities.db"
    }
}