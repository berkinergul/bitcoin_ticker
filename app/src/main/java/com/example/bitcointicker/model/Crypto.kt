package com.example.bitcointicker.model

import com.google.gson.annotations.SerializedName

data class Crypto(

    @SerializedName("id")
    val cryptoID: String,
    @SerializedName("name")
    val cryptoName: String,
    @SerializedName("symbol")
    val cryptoSymbol: String,
    @SerializedName("current_price")
    val cryptoPrice: String,
    @SerializedName("image")
    val cryptoImage: String,
    @SerializedName("description")
    val cryptoDescription: String,

    val isFavourite: Boolean

)