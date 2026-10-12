package com.example.criminalintent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch



class CrimeListFragment : Fragment() {
    private lateinit var adapter: crimeAdapter

    //creates a container to hold the row layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        //creates view hierarchy for layout
        return inflater.inflate(
            R.layout.fragment_crime_list,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(
            R.id.crimeRecyclerView
        )

        //creates add crime button
        val addCrimeButton = view.findViewById<Button>(R.id.addCrimeButton)

        //listens for click and creates new crime
        addCrimeButton.setOnClickListener {
            val newCrime = Crime(
                title = "New Crime",
                date = "10/04/2026",
                isSolved = false
            )

            viewLifecycleOwner.lifecycleScope.launch {
                //puts new crime in repository
                crimeRepository.insertCrime(newCrime)
                //updates adapter to include new crime
                val crimes = crimeRepository.getCrimes()
                adapter.updateCrimes(crimes)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            crimeRepository.initializeSampleDataIfNeeded()

            //calls repository to get list
            val crimes = crimeRepository.getCrimes()

            adapter = crimeAdapter(
                crimes,
                { crime ->
                    val bundle = Bundle()
                    bundle.putLong("crime_id", crime.id)

                    val detailFragment = CrimeDetailFragment()
                    detailFragment.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit()
                },
                { crime ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        crimeRepository.updateCrime(crime)
                    }
                }
            )
            //creates the adapter
            recyclerView.adapter = adapter

            //establishes a linear layout
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext())
        }

    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            viewLifecycleOwner.lifecycleScope.launch {
                val crimes = crimeRepository.getCrimes()
                adapter.updateCrimes(crimes)
            }
        }
    }
}

