package com.example.bitcointicker.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.bitcointicker.R
import com.example.bitcointicker.databinding.FragmentDetailBinding
import com.example.bitcointicker.util.downloadFromUrl
import com.example.bitcointicker.viewmodel.DetailViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DetailFragment : Fragment() {


    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private var id: String? = null
    private lateinit var auth: FirebaseAuth
    private var counter = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onResume() {
        super.onResume()
        val refreshTimes = resources.getStringArray(R.array.refresh_intervals)
        val refreshArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, refreshTimes)
        binding.refreshDropDown.setAdapter(refreshArrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            id = DetailFragmentArgs.fromBundle(it).argCryptoID
        }

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        observeData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {

        viewModel.chosenCrypto.observe(viewLifecycleOwner) { coin ->

            coin?.let {
                val imageUrl = it.image.large
                binding.txtCryptoNameInDetail.text = it.name
                binding.txtCryptoPrice.text = "₺ ${it.marketData.currentPrice.turkishLiras}"
                binding.txtDescriptionInDetail.text = it.description.en
                binding.cryptoImageInDetail.downloadFromUrl(imageUrl)
                binding.txtCryptoPriceChangeOver24H.text =
                    "${it.marketData.priceChangePercentageIn24H}% over 24 Hours"

                binding.refreshDropDown.setOnItemClickListener { parent, view, position, id ->
                    val item: String = parent.getItemAtPosition(position).toString()
                    when (item) {
                        ("24 Hours") -> binding.txtCryptoPriceInterval.text =
                            "${it.marketData.priceChangePercentageIn24H} %"

                        ("7 Days") -> binding.txtCryptoPriceInterval.text =
                            "${it.marketData.priceChangePercentageIn7D} %"

                        ("14 Days") -> binding.txtCryptoPriceInterval.text =
                            "${it.marketData.priceChangePercentageIn14D} %"

                        ("30 Days") -> binding.txtCryptoPriceInterval.text =
                            "${it.marketData.priceChangePercentageIn30D} %"

                        ("60 Days") -> binding.txtCryptoPriceInterval.text =
                            "${it.marketData.priceChangePercentageIn60D} %"

                        ("200 Days") -> binding.txtCryptoPriceInterval.text =
                            "${it.marketData.priceChangePercentageIn200D} %"

                        ("1 Year") -> binding.txtCryptoPriceInterval.text =
                            "${it.marketData.priceChangePercentageIn1Y} %"

                    }
                }

                addCoinToFavourite(it.id,it.name,it.marketData.currentPrice.turkishLiras.toString(),it.image.large)
            }
        }
        viewModel.downloadChosenCryptoById(id!!)
    }

    fun addCoinToFavourite(id: String,coinName : String, coinPrice: String, coinURL : String) {

        val database = Firebase.firestore
        val userID = auth.currentUser!!.uid
        val favourite = hashMapOf(
            "coinID" to id,
            "coinName" to coinName,
            "coinPrice" to coinPrice,
            "coinURL" to coinURL
        )



        binding.btnFavouriteInDetail.setOnClickListener { v ->
            counter += 1

            println("My counter is $counter")

            if (counter % 2 == 0) { //Delete process
                binding.btnFavouriteInDetail.setImageResource(R.drawable.ic_favorite_border)
                database.collection("favourites")
                    .document(userID)
                    .collection("coins")
                    .document(id)
                    .delete()

            } else { //Adding process
                binding.btnFavouriteInDetail.setImageResource(R.drawable.ic_favorite)
                database.collection("favourites")
                    .document(userID)
                    .collection("coins")
                    .document(id)
                    .set(favourite)
                    .addOnSuccessListener {
                        Log.d("Successful add to favourite", "DocumentSnapshot successfully written!")
                    }.addOnFailureListener { e ->
                        Log.w("Failure add to favourite", e.localizedMessage)
                    }

            }


        }

    }
}