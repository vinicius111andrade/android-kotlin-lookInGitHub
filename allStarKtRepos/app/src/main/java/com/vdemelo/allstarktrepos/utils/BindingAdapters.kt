package com.vdemelo.allstarktrepos.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Created by Vinicius Andrade on 10/26/2021.
 */

@BindingAdapter("numberToText")
fun TextView.bindNumberToText(value: Int?) {
    this.text = value?.let {
        it.toString()
    }
}