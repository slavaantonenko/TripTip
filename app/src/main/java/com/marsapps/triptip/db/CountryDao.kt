package com.marsapps.triptip.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marsapps.triptip.model.CountryModel

@Dao
interface CountryDao {
    @Query("SELECT * from countries_table WHERE isInTravelList=1 ORDER BY beenThere")
    fun getTravelListCountries(): LiveData<List<CountryModel>>

    @Query("SELECT * from countries_table WHERE code NOT LIKE 'PS'")
    fun getCountries(): List<CountryModel>

    @Query("UPDATE countries_table SET isInTravelList=:isInTravelList, beenThere=:isBeenThere, image=:image WHERE id=:id")
    fun updateIsInTravelList(id: Int, isInTravelList: Boolean, isBeenThere: Boolean, image: String)

    @Query("UPDATE countries_table SET beenThere=:beenThere WHERE id=:id")
    fun updateBeenThere(id: Int, beenThere: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(countries: Collection<CountryModel>?)

    @Query("DELETE FROM countries_table")
    fun deleteAll()
}