package com.example.criminalintent

object crimeRepository {

    private lateinit var database: crimeDatabase

    //gives access to crimeDatabase
    fun initialize(context: android.content.Context) {
        database = crimeDatabase.getDatabase(context)
    }

    //creates app sample data
    suspend fun initializeSampleData() {
        database.crimeDao().insertCrime(
            Crime(1, "Stolen Bicycle", "09/28/2026", false)
        )

        database.crimeDao().insertCrime(
            Crime(2, "Missing Phone", "09/29/2026", true)
        )

        database.crimeDao().insertCrime(
            Crime(3, "Broken Window", "09/30/2026", false)
        )
    }

    //check if sample data exists
    suspend fun initializeSampleDataIfNeeded() {
        val crimes = database.crimeDao().getCrimes()

        if (crimes.isEmpty()) {
            initializeSampleData()
        }
    }

    //function returns list when called
    suspend fun getCrimes(): List<Crime> {
        return database.crimeDao().getCrimes()
    }

    //function allows changes to be saved
    suspend fun updateCrime(crime: Crime) {
        database.crimeDao().updateCrime(crime)
    }

    //functions allows crimes to be added
    suspend fun insertCrime(crime: Crime) {
        database.crimeDao().insertCrime(crime)
    }

    //function allows crimes to be deleted
    suspend fun deleteCrime(crime: Crime) {
        database.crimeDao().deleteCrime(crime)
    }
}