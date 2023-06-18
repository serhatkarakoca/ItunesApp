package com.karakoca.itunesapp.util

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.Transition
import com.google.android.material.transition.MaterialSharedAxis

fun createEnterTransition(): Transition {
    val transition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
    transition.interpolator = AccelerateDecelerateInterpolator()
    return transition
}

fun createReturnTransition(): Transition {
    val transition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    transition.interpolator = AccelerateDecelerateInterpolator()
    return transition
}