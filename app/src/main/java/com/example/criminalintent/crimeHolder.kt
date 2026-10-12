package com.example.criminalintent

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class crimeHolder (view: View) : RecyclerView.ViewHolder(view) {

    val titleTextView: TextView = view.findViewById(R.id.crimeTitle)

    val dateTextView: TextView = view.findViewById(R.id.crimeDate)

    val solvedCheckBox: CheckBox = view.findViewById(R.id.crimeSolved)
}