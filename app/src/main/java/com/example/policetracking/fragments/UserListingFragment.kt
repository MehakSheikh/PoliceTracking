package com.example.policetracking.fragments

import com.example.policetracking.viewmodels.LoginActivityViewModel
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.policetracking.R
import com.example.policetracking.utils.Utils
import com.example.policetracking.adapters.DataItemUserListing
import com.example.policetracking.adapters.ListAdapterUsers
import com.example.policetracking.databinding.FragmentUserListingBinding
import com.example.policetracking.models.helper.UserModel
import com.example.policetracking.utils.ItemClickListener
import com.example.policetracking.utils.handleClickOnce


internal class UserListingFragment private constructor() : BaseFragment() {

    private lateinit var mBinding: FragmentUserListingBinding
    private lateinit var mViewModel: LoginActivityViewModel
    private lateinit var mAdapter: ListAdapterUsers
    private var mListUsers: ArrayList<UserModel> = arrayListOf()

    override fun init() {
        setAdapter()
    }

    private fun setAdapter() {
        mBinding.apply {
            mAdapter = ListAdapterUsers(ItemClickListener { model, view ->

                view.handleClickOnce()

                mViewModel.currentUser = model

                requireActivity().supportFragmentManager.beginTransaction()
                    .add(R.id.fl_signup_container, MapsFragment.instance(), Utils.Maps_Fragment)
                    .commit()
            })

            mListUsers.add(
                UserModel(
                    id = "1",
                    name = "Talha"
                )
            )
            mListUsers.add(
                UserModel(
                    id = "2",
                    name = "Ahmed"
                )
            )
            mListUsers.add(
                UserModel(
                    id = "3",
                    name = "Mehak"
                )
            )
            mListUsers.add(
                UserModel(
                    id = "4",
                    name = "Sheikh"
                )
            )

            setAdapterData()

            recyclerViewUsers.adapter = mAdapter
        }
    }

    private fun setAdapterData() {
        mAdapter.submitList(
            mListUsers.map {
                DataItemUserListing.UserItemListing(it)
            }
        )
        mAdapter.notifyDataSetChanged()
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