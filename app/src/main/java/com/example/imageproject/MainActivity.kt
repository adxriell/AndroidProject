package com.example.imageproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: BookDatabaseHelper
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database and adapter
        db = BookDatabaseHelper(this)
        bookAdapter = BookAdapter(db.getAllNotes(), this)

        // Setup RecyclerView
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = bookAdapter

        // Setup the add button to navigate to AddNoteActivity
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddBook::class.java)
            startActivity(intent)
        }
        binding.profilButton.setOnClickListener {
            val intent = Intent(this, profil::class.java)
            startActivity(intent)
        }

    }
    override fun onResume() {
        super.onResume()
        // Refresh the data in the adapter when the activity resumes
        bookAdapter.refreshData(db.getAllNotes())
    }
}