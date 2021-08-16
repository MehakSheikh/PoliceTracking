//package com.example.policetracking.fragments;
//
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.example.policetracking.R;
//
//import org.w3c.dom.Text;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import tech.gusavila92.websocketclient.WebSocketClient;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HomeUserFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class HomeUserFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    LocationManager mLocationManager;
//    private WebSocketClient webSocketClient;
//    TextView tv_connection ;
//    public HomeUserFragment() {
//        // Required empty public constructor
//    }
//    // TODO: Rename and change types and number of parameters
//    public static HomeUserFragment newInstance(String param1, String param2) {
//        HomeUserFragment fragment = new HomeUserFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    /*    if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }*/
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }
//
//        private void createWebSocketClient() {
//        URI uri;
//        try {
//            // Connect to local host
//         //   uri = new URI("ws://10.0.2.2:8080/websocket");  //for emulator
//          uri = new URI("ws://192.168.100.3:8080/websocket");    // for real device
//        }
//        catch (URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//        webSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen() {
//                Log.i("WebSocket", "Session is starting");
//                webSocketClient.send("Hello World!, Mehak");
//            }
//            @Override
//            public void onTextReceived(String s) {
//                Log.i("WebSocket", "Message received");
//                final String message = s;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            TextView textView = findViewById(R.id.tv_connection);
//                            textView.setText(message);
//                        } catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//            @Override
//            public void onBinaryReceived(byte[] data) {
//            }
//            @Override
//            public void onPingReceived(byte[] data) {
//            }
//            @Override
//            public void onPongReceived(byte[] data) {
//            }
//            @Override
//            public void onException(Exception e) {
//                System.out.println(e.getMessage());
//            }
//            @Override
//            public void onCloseReceived() {
//                Log.i("WebSocket", "Closed ");
//                System.out.println("onCloseReceived");
//            }
//        };
//        webSocketClient.setConnectTimeout(10000);
//        webSocketClient.setReadTimeout(60000);
//        webSocketClient.enableAutomaticReconnection(5000);
//        webSocketClient.connect();
//    }
//    public void sendMessage(View view) {
//        Log.i("WebSocket", "Button was clicked");
//        // Send button id string to WebSocket Server
//        switch(view.getId()){
//            case(R.id.dogButton):
//                webSocketClient.send("1");
//                break;
//            case(R.id.catButton):
//                webSocketClient.send("2");
//                break;
//            case(R.id.pigButton):
//                webSocketClient.send("3");
//                break;
//            case(R.id.foxButton):
//                webSocketClient.send("4");
//                break;
//        }
//    }
//    private final LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(final Location location) {
//            //your code here
//        }
//    };
//}