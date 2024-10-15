package com.walmart.ui.ext

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("app:visibility")
fun setVisibility(progressBar: ProgressBar, visibility: Boolean) {
    if (visibility) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
}

@BindingAdapter("app:setText")
fun setText(textView: TextView, errorMessage: String) {
    textView.text = errorMessage
}
