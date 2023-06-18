package com.karakoca.itunesapp.util

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImage")
fun ImageView.loadImage(imageUrl: String?) {
    Glide.with(this)
        .load(imageUrl)
        .into(this)
}

@BindingAdapter("app:setSelected")
fun setSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}

@BindingAdapter("app:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}