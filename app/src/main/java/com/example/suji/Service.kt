package com.example.suji

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

val homeLiveData = MutableLiveData<HomeData>()

data class HomeData(
    val content: String
)
inline fun <T> LiveData<T>.bindNonNull(
    lifecycleOwner: LifecycleOwner,
    crossinline block: (T) -> Unit
) =
    observe(lifecycleOwner, Observer { it?.let(block) })