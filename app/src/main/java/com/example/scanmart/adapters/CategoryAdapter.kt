package com.example.scanmart.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scanmart.R
import com.example.scanmart.activities.DetailsActivity
import com.example.scanmart.domains.CategoryDomain

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_viewholder, parent, false)
        )
    }

    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView = itemView.findViewById<TextView>(R.id.categoryTitleTxtView)
        val categoryImageView = itemView.findViewById<ImageView>(R.id.categoryImgView)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.categoryNameTextView.text = category.Name
        Glide.with(holder.itemView.context)
            .load(category.ImagePath)
            .into(holder.categoryImageView)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private val differCallBack = object: DiffUtil.ItemCallback<CategoryDomain>(){
        override fun areItemsTheSame(oldItem: CategoryDomain, newItem: CategoryDomain): Boolean {
            return oldItem.Id == newItem.Id
        }

        override fun areContentsTheSame(oldItem: CategoryDomain, newItem: CategoryDomain): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

}