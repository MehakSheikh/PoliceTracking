package com.example.policetracking.utils

import android.view.View


class ItemClickListener<T>(
    val clickListener: (model: T, view: View) -> Unit
) {
    fun onClick(model: T, view: View) =
        clickListener(model, view)

}