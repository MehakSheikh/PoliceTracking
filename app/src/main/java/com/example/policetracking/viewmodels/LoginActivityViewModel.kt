package com.example.policetracking.viewmodels

import com.example.policetracking.models.helper.UserModel

internal class LoginActivityViewModel : BaseViewModel() {

    private var mCurrentUser: UserModel? = null

    var currentUser: UserModel?
        get() = mCurrentUser
        set(value) {
            mCurrentUser = value
        }
}