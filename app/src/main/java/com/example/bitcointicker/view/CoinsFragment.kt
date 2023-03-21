package com.example.bitcointicker.view


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitcointicker.R
import com.example.bitcointicker.adapter.CoinsAdapter
import com.example.bitcointicker.databinding.FragmentCoinsBinding
import com.example.bitcointicker.model.CryptoDetailed
import com.example.bitcointicker.viewmodel.CoinsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.appcompat.widget.SearchView


class CoinsFragment : Fragment() {

    private var _binding: FragmentCoinsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CoinsViewModel
    private var coinsAdapter = CoinsAdapter(arrayListOf())
    private var id: String = ""
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.coinsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel = ViewModelProvider(this).get(CoinsViewModel::class.java)

        toolbarEvents()
        observeData()



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {

        viewModel.cryptoCurrency.observe(viewLifecycleOwner) { coins ->
            coins?.let {
                coinsAdapter.refreshScreen(coins)
                binding.coinsRecyclerView.visibility = View.VISIBLE
                binding.coinsRecyclerView.adapter = CoinsAdapter(ArrayList(coins))
                it.forEach {
                    id = it.id
                }
            }
        }

        viewModel.cryptoLoading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                if (it) {
                    binding.coinsRecyclerView.visibility = View.GONE
                    binding.txtError.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE

                } else {
                    binding.coinsRecyclerView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        viewModel.cryptoError.observe(viewLifecycleOwner) { error ->
            error?.let {
                if (it)
                    binding.txtError.visibility = View.VISIBLE
                else
                    binding.txtError.visibility = View.GONE
            }
        }

        viewModel.downloadCryptosById(id)

    }

    fun toolbarEvents() {
        binding.toolBar.inflateMenu(R.menu.options)

        binding.toolBar.setOnMenuItemClickListener { menuItem->

            if (menuItem.itemId == R.id.optionSignOut) {
                auth.signOut()
                val actionToLogin = CoinsFragmentDirections.actionCoinsFragmentToLoginFragment()
                findNavController().navigate(actionToLogin)
            } else if (menuItem.itemId == R.id.optionSearch){

                val searchView = menuItem.actionView as SearchView

                searchView.setIconifiedByDefault(false)
                searchView.queryHint = "Crypto name or symbol"
                searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        search(newText!!)
                        return true
                    }

                })
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun search(text: String) {
        val filteredlist: ArrayList<CryptoDetailed> = ArrayList()

        for (item in viewModel.cryptoCurrency.value!!) {
            if (item.name.contains(text,ignoreCase = true) || item.symbol.contains(text,ignoreCase = true)) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(requireContext(), "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            coinsAdapter.refreshScreen(filteredlist)
            binding.coinsRecyclerView.adapter = CoinsAdapter(filteredlist)
        }
    }
}