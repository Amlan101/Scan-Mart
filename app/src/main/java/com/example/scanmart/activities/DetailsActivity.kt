package com.example.scanmart.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scanmart.adapters.SimilarProductsAdapter
import com.example.scanmart.databinding.ActivityDetailsBinding
import com.example.scanmart.domains.ItemDomain
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var item: ItemDomain? = null
    private var weight = 1
    private lateinit var similarProductsAdapter: SimilarProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundles()
        setVariables()
        initSimilarProducts()
    }

    private fun initSimilarProducts() {
        val myRef = database.getReference("Items")
        Log.d("Reference", "Database Reference: $myRef")

        binding.progressBarSimilarProducts.visibility = View.VISIBLE

        val list = ArrayList<ItemDomain>()
        similarProductsAdapter = SimilarProductsAdapter()
        binding.recyclerViewSimilarProducts.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarProductsAdapter
        }

        if (item == null) {
            Log.e("DetailsActivity", "Item data is null. Cannot load similar products.")
            binding.progressBarSimilarProducts.visibility = View.GONE
            return
        }

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (itemSnapshot in snapshot.children) {
                    val product = itemSnapshot.getValue(ItemDomain::class.java)
                    if (product != null && product.CategoryId == item?.CategoryId) {
                        list.add(product)
                        Log.d(
                            "FirebaseData",
                            "Name: ${product.Title}, ID: ${product.Id}, ImagePath: ${product.ImagePath}"
                        )
                    }
                }
                similarProductsAdapter.differ.submitList(list)
                binding.progressBarSimilarProducts.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error loading similar products: ${error.message}")
                binding.progressBarSimilarProducts.visibility = View.GONE
            }
        })
    }

    private fun setVariables() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        item?.let {
            Glide.with(this)
                .load(it.ImagePath)
                .into(binding.productImgView)

            binding.priceTxtView.text = "${it.Price}$/kg"
            binding.titleTxtView.text = it.Title
            binding.descTxtView.text = it.Description
            binding.ratingBar.rating = it.Stars.toFloat()
            binding.ratingTxtView.text = it.Stars.toString()
            updateTotalPrice()

            binding.addBtn.setOnClickListener {
                weight += 1
                binding.quantityTxtView.text = weight.toString()
                updateTotalPrice()
            }

            binding.minusBtn.setOnClickListener {
                if (weight > 1) {
                    weight -= 1
                }
                binding.quantityTxtView.text = weight.toString()
                updateTotalPrice()
            }
        }
    }

    private fun updateTotalPrice() {
        item?.let {
            val totalPrice = it.Price * weight
            binding.totalPriceTxtView.text = "$${totalPrice}"
        }
    }

    private fun getBundles() {
        item = intent.getParcelableExtra("item")
        if (item == null) {
            Log.e("DetailsActivity", "Item data is missing")
            finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        // Handle the back button press
        finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}