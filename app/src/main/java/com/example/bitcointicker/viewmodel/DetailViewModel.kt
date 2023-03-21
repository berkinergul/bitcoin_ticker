package com.example.bitcointicker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitcointicker.model.CryptoDetailed
import com.example.bitcointicker.service.CryptoAPI
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailViewModel : ViewModel() {

    val chosenCrypto = MutableLiveData<CryptoDetailed>()
    private lateinit var job: Job
    private val BASE_URL = "https://api.coingecko.com"

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Error : ${throwable.localizedMessage}")
    }


    fun downloadChosenCryptoById(id: String){

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)

        job = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

            val response = retrofit.getChosenCryptoByID(id)
            withContext(Dispatchers.Main + coroutineExceptionHandler) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chosenCrypto.value = it
                    }
                }
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}