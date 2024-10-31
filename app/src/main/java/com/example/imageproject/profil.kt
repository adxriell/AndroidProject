package com.example.imageproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.imageproject.databinding.ActivityMainBinding
import com.example.imageproject.databinding.ActivityProfilBinding

class profil : AppCompatActivity() {
    private lateinit var binding: ActivityProfilBinding
    private lateinit var db: BookDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val balik: ImageView = binding.kembali

        balik.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}