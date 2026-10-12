package com.example.criminalintent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface crimeDao {

    //pulls information from the crime table
    @Query("SELECT * FROM Crime")
    suspend fun getCrimes(): List<Crime>

    //insert crimes
    @Insert
    suspend fun insertCrime(crime: Crime)

    //update crimes
    @Update
    suspend fun updateCrime(crime: Crime)

    @Delete
    suspend fun deleteCrime(crime: Crime)

}