package com.ljubenvassilev.cities.cities.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ljubenvassilev.cities.CitiesApplication
import com.ljubenvassilev.cities.cities.entities.CityEntity
import com.ljubenvassilev.cities.cities.repo.CitiesRepository
import javax.inject.Inject

class CitiesViewModel : ViewModel() {
    @Inject
    lateinit var citiesRepository: CitiesRepository
    private val offsetLive = MutableLiveData<Int>()

    init {
        CitiesApplication.appComponent.inject(this)
    }

    //Listing to changes on the offsetLive and trigger pulls from the repo
    val cities: LiveData<List<CityEntity>> = Transformations.switchMap(offsetLive)
    { position ->
        citiesRepository.getCities(offset = position)
    }

    //Propagating changes from the repo to the activity to display loading
    val loadStatus: LiveData<Boolean> = Transformations.switchMap(offsetLive)
    { position ->
        citiesRepository.getLoadingStatus(position)
    }

    //Propagating the errorStatus from the repo to activity
    val errorStatus : LiveData<String> = Transformations.switchMap(offsetLive)
    { position ->
        citiesRepository.getErrorStatus(position)
    }

    fun setInput(position: Int) {
        offsetLive.value = position
    }

    fun refreshDb() {
        citiesRepository.deleteCities()
        offsetLive.value = 0
    }
}