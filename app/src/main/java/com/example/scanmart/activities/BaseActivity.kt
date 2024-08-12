package com.example.scanmart.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.scanmart.R
import com.google.firebase.database.FirebaseDatabase

open class BaseActivity : AppCompatActivity() {

    lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        database = FirebaseDatabase.getInstance()
        // Optional: Set database persistence (offline support)
        database.setPersistenceEnabled(true)



    }
}