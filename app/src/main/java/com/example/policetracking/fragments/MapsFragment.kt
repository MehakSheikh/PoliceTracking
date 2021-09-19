
package com.example.policetracking.fragments

import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.policetracking.R
import com.example.policetracking.databinding.FragmentMapsBinding
import com.example.policetracking.network.ServerRequests
import com.example.policetracking.utils.NetworkConnection
import com.example.policetracking.utils.TinyDB
import com.example.policetracking.utils.Vals
import com.example.policetracking.viewmodels.LatLongRequest
import com.example.policetracking.viewmodels.LoginActivityViewModel
import com.example.policetracking.viewmodels.locationGet.BranchesResponseModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

internal class MapsFragment private constructor() : BaseFragment() {


    private lateinit var mBinding: FragmentMapsBinding
    private lateinit var mViewModel: LoginActivityViewModel
    private var mGoogleMap: GoogleMap? = null
    private var mMarker: Marker? = null
    var longitude: Double? = null
    var latitude: Double? = null

    override fun init() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync {
            mGoogleMap = it
            val handler = Handler()
            val timer = Timer()
            val doAsynchronousTask: TimerTask = object : TimerTask() {
                override fun run() {
                    handler.post {
                        try {
                            //your method here
                            receiveLocation(TinyDB.getInstance().getString(Vals.TOKEN))

                        } catch (e: Exception) {
                        }
                    }
                }
            }
            timer.schedule(doAsynchronousTask, 0, 600000) //execute in every 10 minutes

//            setUpLocation()
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

    private fun setUpLocation(lat: Double?, lng: Double?) {
        val position = LatLng(lat!!, lng!!)
     //   val position = LatLng(24.9073631, 67.0761534)
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

    fun receiveLocation(jwt: String?) {
        val latLongRequest = LatLongRequest()
        latLongRequest.jwt = jwt
        val loginRequest = ServerRequests.getInstance(context).recLatLong(2)
        if (NetworkConnection.isOnline(context)) {
            loginRequest.enqueue(object : Callback<BranchesResponseModel> {
                override fun onResponse(call: Call<BranchesResponseModel>, response: Response<BranchesResponseModel>) {
                    if (response.isSuccessful) {
                        val latitude = response.body()!!.data.location.lat
                        val longitude = response.body()!!.data.location.lng


                        val doubleLat: Double? = latitude.toDouble()
                        val doubleLng: Double? = longitude.toDouble()

                        setUpLocation(doubleLat, doubleLng)
                    }
                }

                override fun onFailure(call: Call<BranchesResponseModel>, t: Throwable) {}
            })
        } else {
            Toast.makeText(context, "Check your Internet", Toast.LENGTH_LONG)
        }
    }
   /* fun receiveLocation(jwt: String?) {
        val latLongRequest = LatLongRequest()
        latLongRequest.jwt = jwt
        *//*   mViewModel.currentUser?.apply {
                id_user = id
            }*//*
        val receiveLocRequest = ServerRequests.getInstance(context).recLatLong(2)
        if (NetworkConnection.isOnline(context)) {
            receiveLocRequest.enqueue(object : Callback<BranchesResponseModel?> {
                override fun onResponse(call: Call<BranchesResponseModel?>, response: Response<BranchesResponseModel?>) {
                    if (response.isSuccessful) {
                        //        locSend("latitude", "longitude");
//        attemptSend("latitude", "longitude");

                        latitude = response.body()!!.lat
                        longitude = response.body()!!.lng

                        setUpLocation(latitude, longitude)
                    }
                }

                override fun onFailure(call: Call<BranchesResponseModel?>, t: Throwable) {}
            })
        } else {
            Toast.makeText(context, "Check your Internet", Toast.LENGTH_LONG)
        }
    }*/
}