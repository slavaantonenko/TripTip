package com.marsapps.triptip.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClientManager {

    companion object {
        private var countryService: CountryService? = null

        fun getCountryServiceInstance(url: String): CountryService? {
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            countryService = retrofit.create<CountryService>(
                CountryService::class.java)
            return countryService
        }
    }
}