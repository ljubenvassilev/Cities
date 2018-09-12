package com.ljubenvassilev.cities.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ljubenvassilev.cities.cities.entities.CityEntity

@Dao
interface CityDao {

    @Query("Select * from " + CityEntity.TABLE_NAME + " LIMIT :limit OFFSET :offset")
    fun loadLocalCities(offset: Int, limit: Int): LiveData<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCities(cities: List<CityEntity>?)

    @Query("Select count(*) from " + CityEntity.TABLE_NAME)
    fun loadCitiesCount(): Long

    @Query("DELETE FROM " + CityEntity.TABLE_NAME)
    fun deleteAll()
}