package com.example.policetracking.fragments

import com.example.policetracking.viewmodels.LoginActivityViewModel
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.policetracking.R
import com.example.policetracking.databinding.FragmentUserListingBinding


internal class UserListingFragment private constructor() : BaseFragment() {

    private lateinit var mBinding: FragmentUserListingBinding
    private lateinit var mViewModel: LoginActivityViewModel

    override fun init() {

    }

    override fun setListeners() {
    }

    override fun setLanguageData() {

    }

    override fun getFragmentLayout() = R.layout.fragment_user_listing

    override fun getViewBinding() {
        mBinding = binding as FragmentUserListingBinding
    }

    override fun getViewModel() {
        mViewModel =
            ViewModelProviders.of(requireActivity()).get(LoginActivityViewModel::class.java)
    }

    override fun observe() {
    }

    override fun setLiveDataValues() {

    }

    override fun onClick(v: View?) {

    }

    companion object {
        @JvmStatic
        fun instance() = UserListingFragment()
    }

}