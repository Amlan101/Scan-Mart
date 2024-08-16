package com.example.scanmart.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scanmart.adapters.BestDealsAdapter
import com.example.scanmart.adapters.CategoryAdapter
import com.example.scanmart.databinding.ActivityMainBinding
import com.example.scanmart.domains.CategoryDomain
import com.example.scanmart.domains.ItemDomain
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCategoryList()
        initBestDealsList()

        binding.fab.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initBestDealsList() {

        // Initialize Firebase Database reference
        val myRef = database.getReference("Items")

        binding.bestDealsProgressBar.visibility = View.VISIBLE

        val list = ArrayList<ItemDomain>()
        val bestDealsAdapter = BestDealsAdapter()
        binding.bestDealsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // Checking if the snapshot has any data
                if (snapshot.exists()) {
                    list.clear() // Clear the list to avoid duplicates

                    // Iterate through the snapshot to retrieve data
                    for (dataSnapshot in snapshot.children) {
                        val item = dataSnapshot.getValue(ItemDomain::class.java)
                        item?.let {
                            list.add(it)
                            Log.d(
                                "FirebaseData",
                                "Name: ${it.Title}, ID: ${it.Id}, ImagePath: ${it.ImagePath}"
                            )
                        }
                    }

                    binding.bestDealsProgressBar.visibility = View.GONE

                    // Update the adapter with the new list
                    if (list.isNotEmpty()) {
                        bestDealsAdapter.differ.submitList(list)
                    }
                } else {
                    binding.bestDealsProgressBar.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.bestDealsProgressBar.visibility = View.GONE
                Log.e("Firebase", "Error fetching data", error.toException())
            }

        })
    }

    private fun initCategoryList() {

        val myRef = database.getReference("Category")
        Log.d("Reference", "Database Reference: $myRef")

        binding.categoryProgressBar.visibility = View.VISIBLE

        val list = ArrayList<CategoryDomain>()
        val categoryAdapter = CategoryAdapter()
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }

        // Fetching data from Firebase
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // Check if the snapshot has any data
                if (snapshot.exists()) {
                    list.clear()  // Clear the list to avoid duplicates

                    // Iterate through the snapshot to retrieve data
                    for (dataSnapshot in snapshot.children) {
                        val category = dataSnapshot.getValue(CategoryDomain::class.java)
                        category?.let {
                            list.add(it)

                            Log.d(
                                "FirebaseData",
                                "Name: ${it.Name}, ID: ${it.Id}, ImagePath: ${it.ImagePath}"
                            )
                        }
                    }

                    binding.categoryProgressBar.visibility = View.GONE

                    // Update the adapter with the new list
                    if (list.isNotEmpty()) {
                        categoryAdapter.differ.submitList(list)
                    }
                } else {
                    binding.categoryProgressBar.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.categoryProgressBar.visibility = View.GONE
                Log.e("Firebase", "Error fetching data", error.toException())
            }

        })

    }

}