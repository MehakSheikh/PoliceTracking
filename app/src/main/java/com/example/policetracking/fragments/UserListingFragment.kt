package com.example.policetracking.fragments

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.policetracking.R
import com.example.policetracking.adapters.DataItemUserListing
import com.example.policetracking.adapters.ListAdapterUsers
import com.example.policetracking.databinding.FragmentUserListingBinding
import com.example.policetracking.models.helper.UserModel
import com.example.policetracking.network.ServerRequests
import com.example.policetracking.utils.ItemClickListener
import com.example.policetracking.utils.NetworkConnection
import com.example.policetracking.utils.Utils
import com.example.policetracking.utils.handleClickOnce
import com.example.policetracking.viewmodels.LoginActivityViewModel
import com.example.policetracking.viewmodels.RanksResponseModel
import com.example.policetracking.viewmodels.UserListing.UserListingModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


internal class UserListingFragment private constructor() : BaseFragment() {

    private lateinit var mBinding: FragmentUserListingBinding
    private lateinit var mViewModel: LoginActivityViewModel
    private lateinit var mAdapter: ListAdapterUsers
    private var mListUsers: ArrayList<UserModel> = arrayListOf()
    var rank_list: List<String> = java.util.ArrayList()
    override fun init() {
        getUsers()

    }

    private fun setAdapter() {
        mBinding.apply {
            mAdapter = ListAdapterUsers(ItemClickListener { model, view ->

                view.handleClickOnce()

                mViewModel.currentUser = model

                requireActivity().supportFragmentManager.beginTransaction()
                        .add(R.id.fl_signup_container, MapsFragment.instance(), Utils.Maps_Fragment)
                        .addToBackStack(Utils.Maps_Fragment)
                        .commit()
            })

        /*    mListUsers.add(
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
            )*/

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

   /* fun getRanks() {
        //   progress.setVisibility(View.VISIBLE);
        val ranks = ServerRequests.getInstance(context).ranks
        if (NetworkConnection.isOnline(context)) {
            ranks.enqueue(object : Callback<RanksResponseModel> {
                override fun onResponse(call: Call<RanksResponseModel>, response: Response<RanksResponseModel>) {
                    if (response.isSuccessful) {
                        for (i in response.body()!!.data.indices) {
                            mListUsers.add(
                                    UserModel(
                                            id = response.body()!!.data[i].id.toString(),
                                            name = response.body()!!.data[i].name
                                    ))

                        }
                        Toast.makeText(activity, "Got all Ranks Successfully", Toast.LENGTH_SHORT)
                                .show()
                    }
                }

                override fun onFailure(call: Call<RanksResponseModel>, t: Throwable) {
                    Toast.makeText(context, "Failure", Toast.LENGTH_LONG)
                }
            })
        } else {
            Toast.makeText(context, "Check your Internet", Toast.LENGTH_LONG)
        }
    }*/
    fun getUsers() {
        //   progress.setVisibility(View.VISIBLE);
        val users = ServerRequests.getInstance(context).users
        if (NetworkConnection.isOnline(context)) {
            users.enqueue(object : Callback<UserListingModel> {
                override fun onResponse(call: Call<UserListingModel>, response: Response<UserListingModel>) {
                    if (response.isSuccessful) {
                        for (i in response.body()!!.data.content.indices) {
                            mListUsers.add(
                                    UserModel(
                                            id = response.body()!!.data.content[i].id.toString(),
                                            name = response.body()!!.data.content[i].name
                                    ))
                        }

                        setAdapter()
                        Toast.makeText(activity, "Got all Users Successfully", Toast.LENGTH_SHORT)
                                .show()
                    }
                }

                override fun onFailure(call: Call<UserListingModel>, t: Throwable) {
                    Toast.makeText(context, "Failure", Toast.LENGTH_LONG)
                }
            })
        } else {
            Toast.makeText(context, "Check your Internet", Toast.LENGTH_LONG)
        }
    }
}

private fun <E> List<E>.add(name: E) {
    TODO("Not yet implemented")
}
