package com.example.bitcointicker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bitcointicker.databinding.ItemCoinBinding
import com.example.bitcointicker.model.CryptoDetailed
import com.example.bitcointicker.util.downloadFromUrl
import com.example.bitcointicker.view.FavouritesFragmentDirections


class FavouritesAdapter(val favouritesList : List<CryptoDetailed>) : RecyclerView.Adapter<FavouritesAdapter.FavouritesHolder>() {
    class FavouritesHolder(val binding: ItemCoinBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesHolder {
        val binding = ItemCoinBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavouritesHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouritesHolder, position: Int) {
        holder.binding.txtCryptoName.text = favouritesList[position].name
        holder.binding.txtCryptoPrice.text = favouritesList[position].marketData.currentPrice.turkishLiras.toString()
        holder.binding.cryptoImage.downloadFromUrl(favouritesList[position].image.large)
        holder.itemView.setOnClickListener {
            val actionToDetail = FavouritesFragmentDirections.actionFavouritesFragmentToDetailFragment(favouritesList[position].id)
            Navigation.findNavController(it).navigate(actionToDetail)
        }
    }

    override fun getItemCount(): Int {
        return favouritesList.size
    }
}