package com.example.traveljournal

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddEntryFragment())
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu?.forEach { menuItem ->
            val actionView = layoutInflater.inflate(R.layout.custom_menu_item, null)
            val textView = actionView.findViewById<TextView>(R.id.custom_menu_item_text)
            textView.text = menuItem.title
            menuItem.actionView = actionView
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_task -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AddEntryFragment())
                    .commit()
                true
            }
            R.id.action_view_tasks -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ViewEntriesFragment())
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
