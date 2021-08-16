package com.example.policetracking.activities;

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
import com.example.policetracking.fragments.LoginFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import tech.gusavila92.websocketclient.WebSocketClient;

public class LoginActivity extends AppCompatActivity implements MyFragmentManager {
    private EditText mInputMessageView;
    private List<Message> mMessages = new ArrayList<Message>();
    // private Socket mSocket;
    TextView toolbar_title ;
    Button btn_send;
    FrameLayout flContainer;
    LocationManager mLocationManager;
    private WebSocketClient webSocketClient;

    /*   {
           try {
               mSocket = IO.socket("http://localhost:8080/topic/greetings");
           } catch (URISyntaxException e) {
               e.printStackTrace();
           }
       }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar_title = findViewById(R.id.toolbar_title);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

     /*   mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000,
                30000, mLocationListener);
        createWebSocketClient();
        */
        replaceFragment(new LoginFragment(), true, false);
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
                        .addToBackStack(f.getClass().getSimpleName())
                        .commitAllowingStateLoss();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_signup_container, f)
                        .addToBackStack(f.getClass().getSimpleName())
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
   /* private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
         //   uri = new URI("ws://10.0.2.2:8080/websocket");  //for emulator
          uri = new URI("ws://192.168.100.3:8080/websocket");    // for real device
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
                            TextView textView = findViewById(R.id.titleText);
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
    public void sendMessage(View view) {
        Log.i("WebSocket", "Button was clicked");
        // Send button id string to WebSocket Server
        switch(view.getId()){
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
        }
    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };*/


}