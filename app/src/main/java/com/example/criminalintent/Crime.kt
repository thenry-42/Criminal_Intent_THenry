package com.example.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI
@Entity
data class Crime (
    @PrimaryKey (autoGenerate = true)
    var id: Long = 0,
    var title: String,
    var date: String,
    var isSolved: Boolean,
    var photoUri: String? = null
)
