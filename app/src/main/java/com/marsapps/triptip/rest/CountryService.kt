package com.marsapps.triptip.rest

import com.marsapps.triptip.main.Constants.REST.PLACES_URL
import com.marsapps.triptip.main.Constants.REST.SELF_URL
import com.marsapps.triptip.model.Country
import com.marsapps.triptip.model.CountryCandidates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryService {
    @GET(SELF_URL)
    fun getCountries(): Call<List<Country>>?

    @GET(PLACES_URL)
    fun getCountryImages(@Query("input") capitalCity: String, @Query("key") key: String): Call<CountryCandidates>?
}