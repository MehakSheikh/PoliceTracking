package com.example.policetracking.models.helper

import android.os.Parcel
import android.os.Parcelable

data class UserModel(
    val id: String?,
    val name: String?
) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }
}