package com.example.bitcointicker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bitcointicker.databinding.ItemCoinBinding
import com.example.bitcointicker.model.CryptoDetailed
import com.example.bitcointicker.view.CoinsFragmentDirections


class CoinsAdapter(var coinsList : ArrayList<CryptoDetailed>) : RecyclerView.Adapter<CoinsAdapter.CoinsHolder>() {
    class CoinsHolder(val binding: ItemCoinBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsHolder {
        val binding = ItemCoinBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CoinsHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinsHolder, position: Int) {

        holder.binding.txtCryptoName.text = coinsList[position].name
        holder.binding.txtCryptoPrice.text = "₺ ${coinsList[position].marketData.currentPrice.turkishLiras}"
        Glide.with(holder.itemView.context).load(coinsList[position].image.large).into(holder.binding.cryptoImage)
        holder.itemView.setOnClickListener {
            val actionToDetail = CoinsFragmentDirections.actionCoinsFragmentToDetailFragment(coinsList[position].id)
            Navigation.findNavController(it).navigate(actionToDetail)
        }

    }
    override fun getItemCount(): Int {
        return coinsList.size
    }

    fun refreshScreen(updatedCryptoList : List<CryptoDetailed>){
        coinsList.clear()
        coinsList.addAll(updatedCryptoList)
        notifyDataSetChanged()
    }


}