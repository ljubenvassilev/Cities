package com.ljubenvassilev.cities.cities.repo.api

import com.ljubenvassilev.cities.cities.entities.CitiesResponse
import com.ljubenvassilev.cities.util.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CitiesAPI {

    @GET(Constants.CITIES_API)
    fun getCities(@Query("limit") limit: Int = 10, @Query("offset") offset: Int): Call<CitiesResponse>
}