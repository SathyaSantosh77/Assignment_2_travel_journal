package com.example.traveljournal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEntryFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_entry, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("TravelJournalPrefs", Context.MODE_PRIVATE)

        val titleEditText: EditText = view.findViewById(R.id.titleEditText)
        val descriptionEditText: EditText = view.findViewById(R.id.descriptionEditText)
        val categorySpinner: Spinner = view.findViewById(R.id.spinner_category)
        val saveButton: Button = view.findViewById(R.id.btn_save_entry)

        val categories = arrayOf("Adventure", "Relaxation", "Business","Family","Cultural","Nature")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            val entry = "$title|$description|$category|$timestamp"

            val entries = sharedPreferences.getStringSet("entries", mutableSetOf())?.toMutableSet()
            entries?.add(entry)

            sharedPreferences.edit().putStringSet("entries", entries).apply()
        }

        return view
    }
}
