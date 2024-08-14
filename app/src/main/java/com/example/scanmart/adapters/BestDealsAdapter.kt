package com.example.scanmart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scanmart.R
import com.bumptech.glide.Glide
import com.example.scanmart.domains.ItemDomain

class BestDealsAdapter: RecyclerView.Adapter<BestDealsAdapter.BestDealsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestDealsViewHolder {
        return BestDealsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_best_deal, parent, false)
        )
    }

    class BestDealsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.titleTxtViewBestDeals)
        var price = itemView.findViewById<TextView>(R.id.priceBestDealsTxtView)
        var img = itemView.findViewById<ImageView>(R.id.imgBestDeals)
    }

    override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.title.text = current.Title
        holder.price.text = current.Price.toString()
        Glide.with(holder.itemView.context)
            .load(current.ImagePath)
            .into(holder.img)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val diffCallBack = object: DiffUtil.ItemCallback<ItemDomain>(){
        override fun areItemsTheSame(oldItem: ItemDomain, newItem: ItemDomain): Boolean {
            return oldItem.Id == newItem.Id
        }

        override fun areContentsTheSame(oldItem: ItemDomain, newItem: ItemDomain): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)

}