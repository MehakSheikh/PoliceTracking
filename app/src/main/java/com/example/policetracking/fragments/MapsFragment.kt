package com.example.policetracking.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.policetracking.R
import com.example.policetracking.databinding.FragmentDefaultBinding
import com.example.policetracking.databinding.FragmentMapsBinding
import com.example.policetracking.viewmodels.LoginActivityViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

internal class MapsFragment private constructor() : BaseFragment() {


    private lateinit var mBinding: FragmentMapsBinding
    private lateinit var mViewModel: LoginActivityViewModel
    private var mGoogleMap: GoogleMap? = null
    private var mMarker: Marker? = null

    override fun init() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync {
            mGoogleMap = it

            setUpLocation()
        }

        setUserData()
    }

    private fun setUserData() {
        mBinding.apply {
            mViewModel.currentUser?.apply {
                txtViewName.text = name
            }
        }
    }

    private fun setUpLocation() {
        val position = LatLng(29.8607, 89.0011)
        mGoogleMap?.clear()
        mMarker = setMarker(position)
        animateCamera(position)
    }

    private fun setMarker(position: LatLng): Marker? {
        return mGoogleMap?.addMarker(
            MarkerOptions().position(position)
        )
    }

    private fun animateCamera(position: LatLng) {
        mGoogleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                position, 13F
            )
        )
    }


    override fun setListeners() {
    }

    override fun setLanguageData() {

    }

    override fun getFragmentLayout() = R.layout.fragment_maps

    override fun getViewBinding() {
        mBinding = binding as FragmentMapsBinding
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
        fun instance() = MapsFragment()
    }
}