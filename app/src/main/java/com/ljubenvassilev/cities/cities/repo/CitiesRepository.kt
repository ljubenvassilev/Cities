package com.ljubenvassilev.cities.cities.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ljubenvassilev.cities.cities.CitiesContract
import com.ljubenvassilev.cities.cities.entities.CitiesResponse
import com.ljubenvassilev.cities.cities.entities.CityEntity
import com.ljubenvassilev.cities.cities.repo.api.CitiesAPI
import com.ljubenvassilev.cities.db.CityDao
import com.ljubenvassilev.cities.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CitiesRepository @Inject constructor(
        val cityDao: CityDao,
        val citiesService: CitiesAPI,
        val preferences: SharedPreferences
) : CitiesContract.Repository {

    private val offsetLive = MutableLiveData<Int>()
    private val loadStatusLive = MutableLiveData<Boolean>()
    private val errorStatusLive = MutableLiveData<String>()

    val cities: LiveData<List<CityEntity>> = Transformations.switchMap(offsetLive)
    { offset ->
        cityDao.loadLocalCities(offset, Constants.LIMIT)
    }

    override fun getCities(offset: Int): LiveData<List<CityEntity>> {
        offsetLive.value = offset
        Thread(Runnable {
            val dbCount = cityDao.loadCitiesCount()
            //If the db count is less than the max remote cities count received from API,
            // run a remote query
            if (dbCount < preferences.getInt(Constants.MAX_REMOTE_CITIES_COUNT, Int.MAX_VALUE)) {
                getRemoteCities(offset, Constants.LIMIT)
            } else if (dbCount <= offset) loadStatusLive.postValue(false)
        }).start()

        return cities
    }

    override fun getRemoteCities(offset: Int, limit: Int) {

        val call: Call<CitiesResponse> = citiesService.getCities(limit, offset)
        call.enqueue(object : Callback<CitiesResponse> {
            override fun onResponse(call: Call<CitiesResponse>?, response: Response<CitiesResponse>?) {
                if (response!!.isSuccessful) {
                    val citiesResponse = response.body() ?: return
                    Thread(Runnable {
                        cityDao.insertAllCities(citiesResponse.cities)
                        preferences
                                .edit()
                                .putInt(Constants.MAX_REMOTE_CITIES_COUNT,
                                        citiesResponse.cMetaResponse.totalCount)
                                .apply()
                    }).start()
                } else errorStatusLive.postValue("Error! Server returned: ${response.code()}")
            }

            override fun onFailure(call: Call<CitiesResponse>?, t: Throwable?) {
                if (t is IOException)
                    errorStatusLive.postValue("Error! Please check internet connection!")
                else errorStatusLive.postValue("Error. ${t?.localizedMessage ?: "Unknown"}")
            }

        })
    }


    override fun deleteCities() {
        Thread(Runnable {
            cityDao.deleteAll()
        }).start()
    }

    override fun getLoadingStatus(position: Int?): LiveData<Boolean> {
        return loadStatusLive
    }

    override fun getErrorStatus(position: Int?): LiveData<String> {
        return errorStatusLive
    }

}