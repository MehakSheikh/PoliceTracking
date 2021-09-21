package com.example.policetracking.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;


import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.policetracking.R;
import com.example.policetracking.activities.ExampleService;
import com.example.policetracking.utils.NetworkConnection;
import com.example.policetracking.utils.TinyDB;
import com.example.policetracking.utils.Vals;
import com.example.policetracking.viewmodels.LatLongRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.content.ContentValues.TAG;

public class HomeFragment extends CoreFragment implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    private static final int REQUEST_PERMISSION_LOCATION = 1002;
    Button btn_share_loc, btn_logout;
    public TextView tv_txt;

    String longitude, latitude;
    private static FragmentManager fragmentManager;

    public HomeFragment() {
    }

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://194.163.158.81:3000");
//            mSocket = IO.socket("http://localhost:3000");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        tv_txt = (TextView) view.findViewById(R.id.tv_txt);
        btn_share_loc = (Button) view.findViewById(R.id.btn_share_loc);
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        fragmentManager = getActivity().getSupportFragmentManager();
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new HomeFragment();
        tv_txt.setText("");

        btn_share_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.isOnline(getContext())) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                        requestPermissions(perms, REQUEST_PERMISSION_LOCATION);
                    } else {
                        // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, (float) 0.5, locationListener);

                 /*   mSocket.on("location_send", onNewMessage);
                    mSocket.connect();
                    attemptSend("latitude", "longitude");*/
                        tv_txt.setText("Your location is being shared");
                        getActivity().startService(new Intent(getActivity(), ExampleService.class));
                    }
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext(),R.style.AlertDialog)
                            .setTitle("Check your Internet Connection")
                            //  .setMessage("Are you sure you want to exit?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton(null, null).show();
//            Toast.makeText(getActivity(), "Login Started.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB.getInstance().putString(Vals.TOKEN, "");
                TinyDB.getInstance().putString(Vals.USER_TYPE, "");
                mSocket.disconnect();
                mSocket.off("location_send", onNewMessage);
                getActivity().stopService(new Intent(getActivity(), ExampleService.class));
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                        .replace(R.id.fl_signup_container, new LoginFragment())
                        .commitAllowingStateLoss();
            }
        });
        return view;
    }

    @Override
    public void onLocationChanged(@NonNull Location loc) {
        longitude = String.valueOf(loc.getLongitude());
        Log.v(TAG, longitude);
        latitude = String.valueOf(loc.getLatitude());
        Log.v(TAG, latitude);
        String s = longitude + "\n" + latitude;
        Log.i("LOCATION", s);
        //  tv_txt.setText(latitude + ", " + longitude);
        // attemptSend(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    public void attemptSend(String lat, String lng) {
        TinyDB.dbContext = getContext();
        Gson gson = new Gson();
        LatLongRequest latLongRequest = new LatLongRequest();
        latLongRequest.setLatitude(lat);
        latLongRequest.setLongitude(lng);
        latLongRequest.setJwt(TinyDB.getInstance().getString(Vals.TOKEN));
        latLongRequest.setUserId((long) 2);
        String message = "Hello Abc";
        if (TextUtils.isEmpty(message)) {
            return;
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(gson.toJson(latLongRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("location_send", obj);
        //   tv_txt.setText("Your location is Sharing");
        Log.i("Hello", "Hello Mehak! It is working");
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String lat;
                    String lng;
                    try {
                        lat = data.getString("lat");
                        lng = data.getString("lng");
                    } catch (JSONException e) {
                        return;
                    }
                    // add the message to view
                    //   addMessage(username, message);
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("location_send", onNewMessage);
    }
}