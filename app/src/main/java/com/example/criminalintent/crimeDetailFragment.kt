package com.example.criminalintent

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CrimeDetailFragment : Fragment() {

    private lateinit var photoImageView: ImageView
    private lateinit var photoUri: Uri
    private lateinit var crime: Crime

    //calls camera
    private val takePicture =
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                //stores phot in crime object
                crime.photoUri = photoUri.toString()
                //displays photo
                photoImageView.setImageURI(photoUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Creates a detail fragment view
        val view = inflater.inflate(
            R.layout.fragment_crime_detail,
            container,
            false
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //retrieves crime positon
        val crimeId = arguments?.getLong("crime_id") ?: 0

        val photoFile = File(
            requireContext().filesDir,
            "crime_${crimeId}.jpg"
        )

        //stores file in photoUri
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )

        val titleEditText = view.findViewById<EditText>(R.id.crimeDetailTitle)
        val dateTextView = view.findViewById<TextView>(R.id.crimeDetailDate)
        photoImageView = view.findViewById<ImageView>(R.id.crimeDetailPhoto)
        val solvedCheckBox = view.findViewById<CheckBox>(R.id.crimeDetailSolved)
        val deleteCrimeButton = view.findViewById<Button>(R.id.deleteCrimeButton)
        val shareCrimeButton = view.findViewById<Button>(R.id.shareCrimeButton)

        //set click listener for photo
        photoImageView.setOnClickListener {
            takePicture.launch(photoUri)
        }

        //creates coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            //retrieves data from repository
            val crimes = crimeRepository.getCrimes()
            crime = crimes.first {it.id == crimeId}

            //creates text for sharable crime report
            val shareText = """
            Crime Report
            
            Title: ${crime.title}
            Date: ${crime.date}
            Solved: ${crime.isSolved}
            """.trimIndent()

            titleEditText.setText(crime.title)

            //Update the crime title if it is changed
            titleEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    crime.title = titleEditText.text.toString()

                    viewLifecycleOwner.lifecycleScope.launch {
                        crimeRepository.updateCrime(crime)
                    }
                }
            }

            dateTextView.text = crime.date

            //Set click listener to display calendar component
            dateTextView.setOnClickListener {
                //splits parts of data into separate values
                val parts = crime.date.split("/")
                //turns data parts to integers
                val month = parts[0].toInt()
                val day = parts[1].toInt()
                val year = parts[2].toInt()

                //creates datepicker dialog
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, selectedYear, selectedMonth, selectedDay ->
                        //fixes month value
                        val monthNumber = selectedMonth + 1

                        //reassemble the date
                        val newDate = String.format(
                            "%02d/%02d/%04d", monthNumber, selectedDay, selectedYear)

                        crime.date = newDate
                        dateTextView.text = newDate

                        viewLifecycleOwner.lifecycleScope.launch {
                            crimeRepository.updateCrime(crime)
                        }

                    }, year, month - 1, day
                )
                //displays data picker dialog
                datePickerDialog.show()
            }

            solvedCheckBox.isChecked = crime.isSolved

            //when user checks box, crime status changes in object
            solvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }

            //loads a photo for crime (if there is one)
            if (crime.photoUri != null) {
                photoImageView.setImageURI(Uri.parse(crime.photoUri))
            }

            //set click listener for delete button
            deleteCrimeButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    crimeRepository.deleteCrime(crime)
                    parentFragmentManager.popBackStack()
                }
            }

            shareCrimeButton.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                startActivity(Intent.createChooser(shareIntent, "Share Crime"))
            }
        }

    }

    //save changes to room
    override fun onPause() {
        super.onPause()

        if (::crime.isInitialized) {
            viewLifecycleOwner.lifecycleScope.launch {
                crimeRepository.updateCrime(crime)
            }
        }
    }
}
