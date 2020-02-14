package com.marsapps.triptip.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.marsapps.triptip.model.CountryModel

@Database(entities = [CountryModel::class], version = 1)
abstract class DB : RoomDatabase() {

    abstract fun countryDao(): CountryDao?

    companion object {
        private var INSTANCE: DB? = null

        fun getInstance(context: Context): DB? {
            INSTANCE ?: run {
                synchronized(DB::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, DB::class.java, "room_database")
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return INSTANCE
        }

        fun closeConnection() {
            INSTANCE.let {
                if (INSTANCE!!.isOpen) INSTANCE!!.close()
            }
        }
    }
}