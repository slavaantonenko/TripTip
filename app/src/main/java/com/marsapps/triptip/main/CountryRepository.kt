package com.marsapps.triptip.main

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.marsapps.triptip.db.CountryDao
import com.marsapps.triptip.db.DB
import com.marsapps.triptip.model.CountryModel

class CountryRepository(application: Application) {
    private var countryDao: CountryDao? = null

    init {
        val db: DB? = DB.getInstance(application)
        countryDao = db?.countryDao()
    }

    fun getTravelListCountries(): LiveData<List<CountryModel>>? {
        return countryDao?.getTravelListCountries()
    }

    fun updateIsInTravelList(country: CountryModel) {
        UpdateIsInTravelListAsyncTask(countryDao).execute(country)
    }

    fun updateBeenThere(country: CountryModel) {
        UpdateBeenThereAsyncTask(countryDao).execute(country)
    }

    private class UpdateIsInTravelListAsyncTask internal constructor(dao: CountryDao?) : AsyncTask<CountryModel, Void?, Void?>() {
        private val asyncTaskDao: CountryDao? = dao

        override fun doInBackground(vararg countries: CountryModel): Void? {
            asyncTaskDao?.updateIsInTravelList(countries[0].id, countries[0].isInTravelList,
                countries[0].beenThere, countries[0].image)
            return null
        }

    }

    private class UpdateBeenThereAsyncTask internal constructor(dao: CountryDao?) : AsyncTask<CountryModel, Void?, Void?>() {
        private val asyncTaskDao: CountryDao? = dao

        override fun doInBackground(vararg countries: CountryModel): Void? {
            asyncTaskDao?.updateBeenThere(countries[0].id, countries[0].beenThere)
            return null
        }

    }
}