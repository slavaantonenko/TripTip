package com.marsapps.triptip.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.marsapps.triptip.main.CountryRepository
import com.marsapps.triptip.model.CountryModel

class CountryViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: CountryRepository =
        CountryRepository(application)

    fun getTravelListCountries(): LiveData<List<CountryModel>>? {
        return repository.getTravelListCountries()
    }

    fun updateIsInTravelList(country: CountryModel) {
        repository.updateIsInTravelList(country)
    }

    fun updateBeenThere(country: CountryModel) {
        repository.updateBeenThere(country)
    }

}