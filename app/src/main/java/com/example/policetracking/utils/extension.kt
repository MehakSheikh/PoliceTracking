package com.example.policetracking.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast

fun Context.Toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun View.handleClickOnce() {
    isEnabled = false

    Looper.myLooper()?.let {
        Handler(it).postDelayed({
            isEnabled = true
        }, 1000)
    }
}