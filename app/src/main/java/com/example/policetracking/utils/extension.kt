package com.example.policetracking.utils

import android.content.Context
import android.widget.Toast

fun Context.Toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}