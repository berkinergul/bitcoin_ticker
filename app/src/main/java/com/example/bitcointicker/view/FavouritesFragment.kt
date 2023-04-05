package com.example.bitcointicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bitcointicker.R
import com.example.bitcointicker.adapter.FavouritesAdapter
import com.example.bitcointicker.databinding.FragmentDetailBinding
import com.example.bitcointicker.databinding.FragmentFavouritesBinding
import com.example.bitcointicker.model.*
import com.example.bitcointicker.viewmodel.DetailViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private val favouritesAdapter = FavouritesAdapter(listOf())
    private lateinit var coinID : String

    /*private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition
            val selectedProduct = favouritesAdapter.favouritesList[layoutPosition]
            cryptoDetail!!.deleteCoinFromFirestore(coinID,database)
            favouritesAdapter.notifyItemChanged(favouritesAdapter.favouritesList.size)
        }

    }*/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favouriteCoinsList.layoutManager = LinearLayoutManager(requireContext())
        //ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.favouriteCoinsList)
        readFavouriteCoinsFromFirestore()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun readFavouriteCoinsFromFirestore(){
        val userID = auth.currentUser!!.uid
        val source = Source.CACHE

        database.collection("favourites")
            .document(userID)
            .collection("coins")
            .get(source)
            .addOnSuccessListener { result ->
                if (result.isEmpty)
                    Toast.makeText(requireContext(), "There is no liked crypto", Toast.LENGTH_LONG).show()
                else{
                    val favouriteCoins = mutableListOf<CryptoDetailed>()

                    val description = Description("englishDescription")

                    for(coins in result){
                        val favouritesData = coins.data
                        coinID = favouritesData.get("coinID").toString()
                        val coinName = favouritesData.get("coinName").toString()
                        val coinPrice = favouritesData.get("coinPrice").toString()
                        val coinURL = favouritesData.get("coinURL").toString()
                        val image = Image("","",coinURL)
                        val currentPrice = CurrentPrice(coinPrice.toDouble())
                        val marketData = MarketData(currentPrice,0.0,0.0,0.0,0.0,0.0,0.0,0.0)
                        val crypto = CryptoDetailed(coinID,"coinSymbol",coinName,"coinHash",image,marketData,description)
                        favouriteCoins.add(crypto)

                    }
                    binding.favouriteCoinsList.adapter = FavouritesAdapter(favouriteCoins)

                }
            }
    }

    fun testFetchData(){
        val userID = auth.currentUser!!.uid
        val source = Source.CACHE

        database.collection("favourites").addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.localizedMessage , Toast.LENGTH_SHORT).show()
            }
            if (value != null){
                for(document in value.documents){
                    val favouritesData = document.data
                    val coinName = favouritesData!!.get("coinName")
                    val coinPrice = favouritesData.get("coinPrice")
                    val coinURL = favouritesData.get("coinURL")

                }
            }
        }

    }
}