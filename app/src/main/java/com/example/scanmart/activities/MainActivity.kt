package com.example.scanmart.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.scanmart.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initLocation()
        initCategoryList()

    }

    private fun initCategoryList() {

    }

//    private fun initLocation() {
//        DatabaseReference myref = database.getReference("location")
//
//    }
}