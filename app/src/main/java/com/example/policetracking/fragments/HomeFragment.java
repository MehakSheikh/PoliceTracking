package com.example.policetracking.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.policetracking.R;
import com.example.policetracking.network.ServerRequests;
import com.example.policetracking.utils.NetworkConnection;
import com.example.policetracking.utils.TinyDB;
import com.example.policetracking.utils.Utils;
import com.example.policetracking.utils.Vals;
import com.example.policetracking.viewmodels.LatLongRequest;
import com.example.policetracking.viewmodels.LoginRequest;
import com.example.policetracking.viewmodels.LoginResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

import static android.content.ContentValues.TAG;

public class HomeFragment extends CoreFragment implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView editLocation;
    private WebSocketClient webSocketClient;
    private static final int REQUEST_PERMISSION_LOCATION = 1002;

    public HomeFragment() {
        // Required empty public constructor
    }
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://194.163.158.81:3000");
        } catch (URISyntaxException e) {}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket.on("location_send", onNewMessage);
        mSocket.connect();
        sendMessage("12.12121212" +"", "34.343434"+"");
     //   createWebSocketClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        editLocation =(TextView) view.findViewById(R.id.textview1);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new HomeFragment();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION};
            requestPermissions(perms, REQUEST_PERMISSION_LOCATION);
        }
        else
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, (float) 0.5, locationListener);

        return view;
    }

    @Override
    public void onLocationChanged(@NonNull Location loc) {
        //your code here
        String longitude = "Longitude: " + loc.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v(TAG, latitude);
        String s = longitude + "\n" + latitude ;
        Log.i("LOCATION", s);
        sendMessage(loc.getLongitude() +"", loc.getLatitude()+"");
/*     //   editLocation.setText("");
        // pb.setVisibility(View.INVISIBLE);

        String longitude = "Longitude: " + loc.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v(TAG, latitude);

        *//*------- To get city name from coordinates -------- *//*
      *//*  String cityName = null;
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                + cityName;
      *//*
       // editLocation.setText(s);
        String s = longitude + "\n" + latitude ;
        Log.i("LOCATION", s);
       // Toast.makeText(getContext(),"Location changed" + s , Toast.LENGTH_LONG  );
    //    sendLocation(loc.getLongitude()+"", loc.getLatitude()+"");*/
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

    public void sendLocation(String lng, String lat){
        LatLongRequest latLongRequest = new LatLongRequest();
        latLongRequest.setLatitude(lat);
        latLongRequest.setLongitude(lng);
        final Call<LoginResponse> loginRequest = ServerRequests.getInstance(getContext()).sendLatLong(latLongRequest);
       // if (NetworkConnection.isOnline(getContext())) {
            loginRequest.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                    //    Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.i("Failure", "failure");
               //     Toast.makeText(getContext(), "Check you Internet", Toast.LENGTH_LONG);
                }
            });
       /* } else {
            Log.i("Failure", "failure");
          //  Toast.makeText(getContext(), "Check you Internet", Toast.LENGTH_LONG);
        }*/

    }
    public void sendMessage(String lat, String lng) {
        Gson gson = new Gson();
        Log.i("WebSocket", "Send Lat Lng");
        LatLongRequest latLongRequest = new LatLongRequest();
        latLongRequest.setLatitude(lat);
        latLongRequest.setLongitude(lng);
//        latLongRequest.setAction("location_send");
        latLongRequest.setJwt("kajygkasgyfas7faf");
        latLongRequest.setUserId((long) 1);
        //  String convert= latLongRequest.toString();
        String abc = latLongRequest.toString();
        JSONObject obj = null;
        try {
            obj = new JSONObject(gson.toJson(latLongRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        attemptSend();
     //   webSocketClient.send(obj.toString());
        // Send button id string to WebSocket Server
      /*  switch(view.getId()){
            case(R.id.dogButton):
                webSocketClient.send("1");
                break;
            case(R.id.catButton):
                webSocketClient.send("2");
                break;
            case(R.id.pigButton):
                webSocketClient.send("3");
                break;
            case(R.id.foxButton):
                webSocketClient.send("4");
                break;
        }*/
    }
    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            //   uri = new URI("ws://10.0.2.2:8080/websocket");  //for emulator
            uri = new URI("http://194.163.158.81:3000");    // for real device
//          uri = new URI("ws://192.168.100.3:8080/websocket");    // for real device
//           uri = new URI("wss://tomcat-server88.paybot.pk:8080/police-tracking/websocket");    // for real device
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Hello World!, Mehak");
            }
            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            TextView textView =getView().findViewById(R.id.textview1);
                            textView.setText(message);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            @Override
            public void onBinaryReceived(byte[] data) {
            }
            @Override
            public void onPingReceived(byte[] data) {
            }
            @Override
            public void onPongReceived(byte[] data) {
            }
            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    private EditText mInputMessageView;

    public void attemptSend() {
        TinyDB.dbContext = getContext();
        Gson gson = new Gson();
        LatLongRequest latLongRequest = new LatLongRequest();
        latLongRequest.setLatitude("87.234234234");
        latLongRequest.setLongitude("23.234234");
//        latLongRequest.setAction("location_send");
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
     //   mInputMessageView.setText("");
        mSocket.emit("location_send", obj);
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