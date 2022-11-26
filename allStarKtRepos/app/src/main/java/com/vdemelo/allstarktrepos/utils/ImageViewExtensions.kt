package com.vdemelo.allstarktrepos.utils

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vdemelo.allstarktrepos.R

fun ImageView.loadImage(uri: String?) {
    val options = RequestOptions()
        .placeholder(getProgressDrawable(context))
        .error(R.drawable.ic_account_circle)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

private fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}
