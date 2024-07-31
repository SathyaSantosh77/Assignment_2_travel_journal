package com.example.traveljournal

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class ViewEntriesFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var entriesContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Enable options menu for sorting
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_entries, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("TravelJournalPrefs", Context.MODE_PRIVATE)

        entriesContainer = view.findViewById(R.id.entriesContainer)
        displayEntries()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.view_entries_menu, menu) // Inflate the menu for sorting
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_date -> {
                displayEntries(sortedByDate = true)
                true
            }
            R.id.action_sort_by_title -> {
                displayEntries(sortedByTitle = true)
                true
            }
            R.id.action_sort_by_category -> {
                displayEntries(sortedByCategory = true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun displayEntries(sortedByDate: Boolean = false, sortedByTitle: Boolean = false, sortedByCategory: Boolean = false) {
        val entries = sharedPreferences.getStringSet("entries", setOf())?.toMutableList() ?: mutableListOf()

        if (sortedByDate) {
            entries.sortByDescending { entry ->
                val parts = entry.split("|")
                parts.getOrNull(3) ?: ""
            }
        } else if (sortedByTitle) {
            entries.sortBy { entry ->
                val parts = entry.split("|")
                parts.getOrNull(0) ?: ""
            }
        } else if (sortedByCategory) {
            entries.sortBy { entry ->
                val parts = entry.split("|")
                parts.getOrNull(2) ?: ""
            }
        }

        entriesContainer.removeAllViews()
        entries.forEach { entry ->
            val parts = entry.split("|")
            if (parts.size == 4) {
                val entryView = LayoutInflater.from(context).inflate(R.layout.entry_item, entriesContainer, false)
                val titleTextView: TextView = entryView.findViewById(R.id.titleTextView)
                val descriptionTextView: TextView = entryView.findViewById(R.id.descriptionTextView)
                val categoryTextView: TextView = entryView.findViewById(R.id.categoryTextView)
                val timestampTextView: TextView = entryView.findViewById(R.id.timestampTextView)
                val deleteButton: Button = entryView.findViewById(R.id.deleteButton)

                titleTextView.text = "Title: ${parts[0]}"
                descriptionTextView.text = "Description: ${parts[1]}"
                categoryTextView.text = "Category: ${parts[2]}"
                timestampTextView.text = "Timestamp: ${parts[3]}"

                deleteButton.setOnClickListener {
                    entries.remove(entry)
                    sharedPreferences.edit().putStringSet("entries", entries.toSet()).apply()
                    displayEntries(sortedByDate, sortedByTitle, sortedByCategory) // Refresh the entries
                }

                entriesContainer.addView(entryView)
            }
        }

        if (entries.isEmpty()) {
            val noEntriesTextView = TextView(context)
            noEntriesTextView.text = "No entries found."
            entriesContainer.addView(noEntriesTextView)
        }
    }
}
