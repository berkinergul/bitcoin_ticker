package com.example.bitcointicker.service

import com.example.bitcointicker.model.Crypto
import com.example.bitcointicker.model.CryptoDetailed
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CryptoAPI {

    //https://pro-api.coingecko.com/api/v3/


    @GET("/api/v3/coins/{id}")
    suspend fun getCryptosByID(@Path("id") id : String) : Response<List<CryptoDetailed>>

    @GET("/api/v3/coins/{id}")
    suspend fun getChosenCryptoByID(@Path("id") id : String) : Response<CryptoDetailed>

}