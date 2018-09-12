package com.ljubenvassilev.cities.cities

import androidx.lifecycle.LiveData
import com.ljubenvassilev.cities.cities.entities.CityEntity

interface CitiesContract {
    interface Repository {
        fun getCities(offset: Int): LiveData<List<CityEntity>>
        fun getRemoteCities(offset: Int, limit: Int)
        fun deleteCities()
        fun getLoadingStatus(position: Int?): LiveData<Boolean>
        fun getErrorStatus(position: Int?): LiveData<String>
    }
}