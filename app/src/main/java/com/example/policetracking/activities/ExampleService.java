package com.example.policetracking.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.policetracking.R;
import com.example.policetracking.fragments.AdminMenuFragment;
import com.example.policetracking.fragments.HomeFragment;
import com.example.policetracking.network.ServerRequests;
import com.example.policetracking.utils.NetworkConnection;
import com.example.policetracking.utils.TinyDB;
import com.example.policetracking.utils.Vals;
import com.example.policetracking.viewmodels.LatLongRequest;
import com.example.policetracking.viewmodels.LoginRequest;
import com.example.policetracking.viewmodels.LoginResponse;
import com.example.policetracking.viewmodels.ReceiveLocationResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ExampleService extends Service implements LocationListener {
    public static final String CHANNEL_ID = "exampleServiceChannel";
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    String longitude, latitude;
    private Socket mSocket;
    public static Context context;
   /* {
        try {
            mSocket = IO.socket("http://194.163.158.81:3000");
//            mSocket = IO.socket("http://localhost:3000");
        } catch (URISyntaxException e) {
        }
}*/

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new ExampleService();
        context = this;
        // mSocket.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Your Location is being shared")
                .setContentText(input)
                .setSmallIcon(R.drawable.icon_location)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            //  mSocket.on("location_send", onNewMessage);
            //  attemptSend("lat", "lng");

        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Your Location is being shared",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.i("Mehak", "location changes");
        longitude = String.valueOf(location.getLongitude());
        Log.v(TAG, longitude);
        latitude = String.valueOf(location.getLatitude());
        Log.v(TAG, latitude);
        String s = longitude + "\n" + latitude;
        Log.i("LOCATION", s);
//        locSend("latitude", "longitude");
//        attemptSend("latitude", "longitude");
        sendLocation(latitude, longitude);
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
//SOCKET WORKING
 /*   public void attemptSend(String lat, String lng) {
        TinyDB.dbContext = getApplication();
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
        Log.i("Hello", "Hello Mehak! It is working");
    }

    public void locSend(String lat, String lng){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {

                    attemptSend(lat, lng);
                } catch (Exception e) {

                }
                return null;
            }
        }.execute();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            Log.e(TAG, "call: driverLocation return in rider app " + args[0].toString());

            // Log.e(TAG, "call: driverLocation return in rider app longitude "+args[1].toString());
            try {
                final JSONObject obj = (JSONObject) args[0];
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        return null;
                    }
                }.execute();

            } catch (Exception ex) {

            }
        }

    };*/

    public void sendLocation(String lat, String lng) {
        LatLongRequest latLongRequest = new LatLongRequest();
        latLongRequest.setLatitude(lat);
        latLongRequest.setLongitude(lng);
        latLongRequest.setJwt(TinyDB.getInstance().getString(Vals.TOKEN));
        final Call<LoginResponse> sendLocRequest = ServerRequests.getInstance(context).sendLatLong(latLongRequest);
        if (NetworkConnection.isOnline(context)) {
            sendLocRequest.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        Log.i("Hello", "Hello Mehak! It is working");
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                }
            });
        } else {
            //      Toast.makeText(this, "Check your Internet", Toast.LENGTH_LONG);
        }
    }

}