package com.example.criminalintent

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView

class crimeAdapter(
    private var crimes: List<Crime>,
    private val onCrimeClicked: (Crime) -> Unit,
    private val onSolveChanged: (Crime) -> Unit) : RecyclerView.Adapter<crimeHolder>() {
    // creates a function to make the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): crimeHolder {
        //builds the rows from XML view (list_item_crimes)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_crimes,parent,false)
        //returns the row template from the view holder
        return crimeHolder(view)
    }
    //creates a function to bind crime class values to text view to be displayed in rows
    override fun onBindViewHolder(holder: crimeHolder, position: Int) {

        //creates variable to hold the crime
        val crime = crimes[position]

        //assigns name of crime to a text view
        holder.titleTextView.text = crime.title
        //assigns date of crime to a text view
        holder.dateTextView.text = crime.date
        //assigns solved status to text view
        //holder.solvedCheckBox.isChecked = crime.isSolved

        // empties on click change listener
        holder.solvedCheckBox.setOnCheckedChangeListener(null)

        // Set checkbox to match current crime
        holder.solvedCheckBox.isChecked = crime.isSolved

        // create listener with variable
        val listener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            crime.isSolved = isChecked
            onSolveChanged(crime)
/*
            //save the changed solved status to Room
            CoroutineScope(Dispatchers.IO).launch {
                crimeRepository.updateCrime(crime)

 */
        }

        //create new listener for checkbox
        holder.solvedCheckBox.setOnCheckedChangeListener(listener)

        // creates listener for row
        holder.itemView.setOnClickListener {
            onCrimeClicked(crime)
        }
    }

    // creates a function to get the count of objects in the crimes list
    override fun getItemCount(): Int {
        //function returns the count of values in the crime list
        return crimes.size
    }

    fun updateCrimes(newCrimes: List<Crime>) {
        crimes = newCrimes
        notifyDataSetChanged()
    }

}