package com.example.policetracking.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.policetracking.Message;
import com.example.policetracking.MyFragmentManager;
import com.example.policetracking.R;
import com.example.policetracking.fragments.AdminMenuFragment;
import com.example.policetracking.fragments.HomeFragment;
import com.example.policetracking.fragments.LoginFragment;
import com.example.policetracking.utils.TinyDB;
import com.example.policetracking.utils.Vals;
import com.example.policetracking.viewmodels.LatLongRequest;
import com.example.policetracking.viewmodels.LatLongRequest2;
import com.example.policetracking.viewmodels.LoginActivityViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import tech.gusavila92.websocketclient.WebSocketClient;

import static android.content.ContentValues.TAG;

public class HomeConstableActivity extends AppCompatActivity implements MyFragmentManager {
    private List<Message> mMessages = new ArrayList<Message>();
    TextView toolbar_title;
    private WebSocketClient webSocketClient;
    private LoginActivityViewModel mViewModel;
    private static final int REQUEST_PERMISSION_LOCATION = 1002;
    LocationManager mLocationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_constable);

        toolbar_title = findViewById(R.id.toolbar_title);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        mViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel.class);
        TinyDB.dbContext = getApplicationContext();
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        createWebSocketClient();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, (float) 0.5, mLocationListener);



    }

    private void addMessage(String username, String message) {
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).build());
        //  mAdapter.notifyItemInserted(mMessages.size() - 1);
        //    scrollToBottom();
    }

    public void replaceFragment(Fragment f, boolean bStack, boolean withTransition) {
        try {
            if (withTransition) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                        .replace(R.id.fl_signup_container, f)
                        //   .addToBackStack(f.getClass().getSimpleName())
                        .commitAllowingStateLoss();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_signup_container, f)
                        //  .addToBackStack(f.getClass().getSimpleName())
                        .commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeTitle(String s) {
        toolbar_title.setText("Login");
    }
    //working
    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            //   uri = new URI("ws://10.0.2.2:8080/websocket");  //for emulator
         //   uri = new URI("wss://194.163.158.81:8080/websocket");    // for real device
//          uri = new URI("ws://192.168.100.3:8080/websocket");    // for real device
           uri = new URI("wss://tomcat-server88.paybot.pk:8080/police-tracking/websocket");    // for real device
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            TextView textView = findViewById(R.id.textview1);
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
    public void sendMessage(String lat, String lng) {
        Gson gson = new Gson();
        Log.i("WebSocket", "Send Lat Lng");
        LatLongRequest latLongRequest = new LatLongRequest();
        latLongRequest.setLatitude(lat);
        latLongRequest.setLongitude(lng);
        latLongRequest.setAction("location_send");
        latLongRequest.setUserId((long) 1);
      //  String convert= latLongRequest.toString();
        String abc = latLongRequest.toString();
        JSONObject obj = null;
        try {
            obj = new JSONObject(gson.toJson(latLongRequest));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webSocketClient.send(obj.toString());
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
    private final LocationListener mLocationListener = new LocationListener() {
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
        }
    };

  /*  public void onLocationChanged(@NonNull Location loc) {
        //   editLocation.setText("");
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
        sendLocation(loc.getLongitude()+"", loc.getLatitude()+"");
    }*/

}