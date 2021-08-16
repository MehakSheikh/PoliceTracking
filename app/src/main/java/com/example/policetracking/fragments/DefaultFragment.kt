package com.example.policetracking.fragments

import LoginActivityViewModel
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.policetracking.R
import com.example.policetracking.databinding.FragmentDefaultBinding


internal class DefaultFragment : BaseFragment() {

    private lateinit var mBinding: FragmentDefaultBinding
    private lateinit var mViewModel: LoginActivityViewModel

    override fun init() {

    }

    override fun setListeners() {
    }

    override fun setLanguageData() {

    }

    override fun getFragmentLayout() = R.layout.fragment_default

    override fun getViewBinding() {
        mBinding = binding as FragmentDefaultBinding
    }

    override fun getViewModel() {
        mViewModel = ViewModelProviders.of(requireActivity()).get(LoginActivityViewModel::class.java)
    }

    override fun observe() {
    }

    override fun setLiveDataValues() {

    }

    override fun onClick(v: View?) {

    }

}