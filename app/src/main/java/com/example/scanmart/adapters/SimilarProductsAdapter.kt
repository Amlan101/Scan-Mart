package com.example.scanmart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scanmart.R
import com.example.scanmart.domains.ItemDomain
import com.bumptech.glide.Glide

class SimilarProductsAdapter: RecyclerView.Adapter<SimilarProductsAdapter.SimilarProductsViewHolder>() {

    class SimilarProductsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgSimilarProduct = itemView.findViewById<ImageView>(R.id.imgSimilarProduct)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimilarProductsViewHolder {
        return SimilarProductsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_similar_products, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: SimilarProductsViewHolder,
        position: Int
    ) {
        val item = differ.currentList[position]

        Glide.with(holder.itemView.context)
            .load(item.ImagePath)
            .into(holder.imgSimilarProduct)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ItemDomain>(){
        override fun areItemsTheSame(oldItem: ItemDomain, newItem: ItemDomain): Boolean {
            return oldItem.Id == newItem.Id
        }

        override fun areContentsTheSame(oldItem: ItemDomain, newItem: ItemDomain): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)
}