package com.example.policetracking.fragments

import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import java.text.SimpleDateFormat
import java.util.*

internal class MapsFragment private constructor() : BaseFragment() {

    private lateinit var mBinding: FragmentMapsBinding
    private lateinit var mViewModel: LoginActivityViewModel
    private var mGoogleMap: GoogleMap? = null
    private var mMarker: Marker? = null

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
            timer.schedule(doAsynchronousTask, 0, 10000) //execute in every 10 minutes
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
                        position, 18F
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

                        if (response.body()!!.data.location != null && response.body()!!.data.location.updatedAt != null && response.body()!!.data.location.updatedAt != "") {
                            val latitude = response.body()!!.data.location.lat
                            val longitude = response.body()!!.data.location.lng

                            val doubleLat: Double? = latitude.toDouble()
                            val doubleLng: Double? = longitude.toDouble()

                            val dateString = response.body()!!.data.location.updatedAt
                            dateFormat(dateString)

                            setUpLocation(doubleLat, doubleLng)
                        } else {
                            val alertDialog = AlertDialog.Builder(context!!, R.style.AlertDialog)
                                    .setTitle("User not updated location") //  .setMessage("Are you sure you want to exit?")
                                    .setPositiveButton("OK") { dialog, which -> }.setNegativeButton(null, null).show()
                           }

                    }
                }

                override fun onFailure(call: Call<BranchesResponseModel>, t: Throwable) {}
            })
        } else {
            Toast.makeText(context, "Check your Internet Connection", Toast.LENGTH_LONG)
        }
    }

    open fun dateFormat(dateString: String?): Unit {
        // Get Current Date Time
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")
        val getCurrentDateTime = sdf.format(c.time)
        Log.d("getCurrentDateTime", getCurrentDateTime)

        val sdf2 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")

        val lastUpdatedDate = sdf2.parse(dateString)
        val currentDate = sdf2.parse(getCurrentDateTime)

        // if ((((currentDate.time - lastUpdatedDate.time) / (1000 * 60)) % 60) > 3){
        if ((currentDate.time - lastUpdatedDate.time) / 60000 > 5) {
            val alertDialog = this!!.context?.let {
                AlertDialog.Builder(it,R.style.AlertDialog)
                        .setTitle("Location not updated for more than 5 minutes") //  .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("OK") { dialog, which -> }.setNegativeButton(null, null).show()
                Log.d("Return", "getMyTime greater than getCurrentDateTime ")
            }
        }
    }
}