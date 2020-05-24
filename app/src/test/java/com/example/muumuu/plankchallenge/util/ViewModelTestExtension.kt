package com.example.muumuu.plankchallenge.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.getValueForTest(): T? {
    var value: T? = null
    val observer = Observer<T> {
        value = it
    }
    observeForever(observer)
    removeObserver(observer)
    return value
}