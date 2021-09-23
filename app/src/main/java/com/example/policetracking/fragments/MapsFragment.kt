package com.example.policetracking.fragments

import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.example.policetracking.R
import com.example.policetracking.databinding.FragmentMapsBinding
import com.example.policetracking.models.helper.UserModel
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

internal class MapsFragment() : BaseFragment() {

    private lateinit var mBinding: FragmentMapsBinding
    private lateinit var mViewModel: LoginActivityViewModel
    private var mGoogleMap: GoogleMap? = null
    private var mMarker: Marker? = null
    var alertDialog: AlertDialog? = null
    var id_user: Int? = null
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

        requireArguments()
        id_user = ((requireArguments().get("KEY_PARSE_DATA") as UserModel).id)!!.toInt()
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
        fun instance(model: UserModel) = MapsFragment().apply {

        }
    }

    fun receiveLocation(jwt: String?) {
        val latLongRequest = LatLongRequest()
        latLongRequest.jwt = jwt
        val loginRequest = this!!.id_user?.let { ServerRequests.getInstance(context).recLatLong(it) }
        if (NetworkConnection.isOnline(context)) {
            loginRequest!!.enqueue(object : Callback<BranchesResponseModel> {
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
                            if (alertDialog != null && alertDialog!!.isShowing) {
                                alertDialog!!.dismiss();
                            }
                            alertDialog = context?.let {
                                AlertDialog.Builder(it, R.style.AlertDialog)
                                        .setTitle("User haven't allowed access to location yet") //  .setMessage("Are you sure you want to exit?")
                                        .setPositiveButton("OK") { dialog, which -> }.setNegativeButton(null, null).show()
                            }
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
        var time: Long = (currentDate.time - lastUpdatedDate.time) / 60000
        var day: Long = 0
        var txt: String

        txt = "$time minutes"
        if (time >= 60) {
            time = time / 60
            txt = "$time hours"
        }
        // if ((((currentDate.time - lastUpdatedDate.time) / (1000 * 60)) % 60) > 3){
        if ((currentDate.time - lastUpdatedDate.time) / 60000 > 15) {
            if (alertDialog != null && alertDialog!!.isShowing) {
                alertDialog!!.dismiss();
            }
            alertDialog = context?.let {
                AlertDialog.Builder(it, R.style.AlertDialog)
                        .setTitle("Location not updated for " + txt + "") //  .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("OK") { dialog, which -> }.setNegativeButton(null, null).show()
            }
        }
    }
}