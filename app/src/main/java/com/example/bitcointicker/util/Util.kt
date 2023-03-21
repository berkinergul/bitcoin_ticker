package com.example.bitcointicker.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bitcointicker.R

fun ImageView.downloadFromUrl(url: String?) {

    val options = RequestOptions()
        .error(R.mipmap.ic_launcher)


    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}