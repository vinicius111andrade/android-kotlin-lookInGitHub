package com.vdemelo.allstarktrepos.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("numberToText")
fun TextView.bindNumberToText(value: Int?) {
    this.text = value?.let {
        it.toString()
    }
}

@BindingAdapter( "imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context))
}
