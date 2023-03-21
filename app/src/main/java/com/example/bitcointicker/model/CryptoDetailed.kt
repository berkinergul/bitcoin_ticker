package com.example.bitcointicker.model

import com.google.gson.annotations.SerializedName

data class CryptoDetailed(

    val id: String,
    val symbol: String,
    val name: String,
    @SerializedName("hashing_algorithm")
    val hashingAlgorithm: String,
    val image: Image,
    @SerializedName("market_data")
    val marketData: MarketData,
    val description: Description
    )


data class Image(
    val thumb: String,
    val small: String,
    val large: String
)

data class MarketData(

    @SerializedName("current_price")
    var currentPrice: CurrentPrice,

    @SerializedName("price_change_percentage_24h")
    var priceChangePercentageIn24H: Double,

    @SerializedName("price_change_percentage_7d")
    val priceChangePercentageIn7D: Double,

    @SerializedName("price_change_percentage_14d")
    val priceChangePercentageIn14D: Double,

    @SerializedName("price_change_percentage_30d")
    val priceChangePercentageIn30D: Double,

    @SerializedName("price_change_percentage_60d")
    val priceChangePercentageIn60D: Double,

    @SerializedName("price_change_percentage_200d")
    val priceChangePercentageIn200D: Double,

    @SerializedName("price_change_percentage_1y")
    val priceChangePercentageIn1Y: Double,
)

data class CurrentPrice(
    @SerializedName("try")
    var turkishLiras: Double
)

data class Description(
    val en: String
)









