package com.example.criminalintent

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Crime::class],
    version = 2
)

abstract class crimeDatabase : RoomDatabase (){
    //connects crime Dao interface
    abstract fun crimeDao(): crimeDao

    companion object {
        private var INSTANCE: crimeDatabase? = null
        //creates the database
        fun getDatabase(context: android.content.Context): crimeDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    crimeDatabase::class.java,
                    "crime-database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return INSTANCE!!
        }
    }
}