package com.example.bitcointicker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitcointicker.model.CryptoDetailed
import com.example.bitcointicker.service.CryptoAPI
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoinsViewModel : ViewModel() {

    val cryptoCurrency = MutableLiveData<List<CryptoDetailed>>()
    val cryptoLoading = MutableLiveData<Boolean>()
    val cryptoError = MutableLiveData<Boolean>()


    private var job: Job? = null
    private val BASE_URL = "https://api.coingecko.com"


    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Error : ${throwable.localizedMessage}")
        cryptoError.value = true
    }

    val coroutineHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }


    fun downloadCryptosById(id: String) {
        cryptoLoading.value = true

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)


        job = viewModelScope.launch(Dispatchers.IO + coroutineHandler) {

            val response = retrofit.getCryptosByID(id)
            Log.i("response: ", response.body().toString())
            withContext(Dispatchers.Main + coroutineHandler) {
                cryptoLoading.value = false
                if (response.isSuccessful) {
                    cryptoError.value = false
                    response.body()?.let {
                        cryptoCurrency.value = it
                    }
                }
            }
        }
    }

    //Prevents memory leak
    override fun onCleared() {
        super.onCleared()
        job?.cancel()

    }


}