package com.example.policetracking.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.policetracking.R;
import com.example.policetracking.utils.TinyDB;
import com.example.policetracking.utils.Utils;
import com.example.policetracking.utils.Vals;
import com.example.policetracking.viewmodels.AdminReq;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class AdminMenuFragment extends CoreFragment {

    Button signUp_form, users_list, btn_logout;

    private static FragmentManager fragmentManager;

    private Socket mSocket;
    Gson gson = new Gson();

    public AdminMenuFragment() {
        // Required empty public constructor
    }

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
//        mSocket.on("subscribe", onNewMessage);
        mSocket.on("location_receive", onNewMessage);

        mSocket.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_menu, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        users_list = (Button) view.findViewById(R.id.users_list);
        signUp_form = (Button) view.findViewById(R.id.signUp_form);
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        AdminReq adminReq = new AdminReq();
        adminReq.setRole("ADMIN");

        JSONObject obj = null;
        try {
            obj = new JSONObject(gson.toJson(adminReq));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("subscribe", obj);

        users_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                        .replace(R.id.fl_signup_container, UserListingFragment.instance())
                        .addToBackStack(Utils.User_Listing_Fragment)
                        .commitAllowingStateLoss();
       /*         fragmentManager
                        .beginTransaction()
                        //  .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.fl_signup_container, UserListingFragment.instance(),
                                Utils.User_Listing_Fragment).commit();*/
            }
        });
        signUp_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                        .replace(R.id.fl_signup_container, new SignupFragment())
                        .addToBackStack(new SignupFragment().getClass().getSimpleName())
                        .commitAllowingStateLoss();

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB.getInstance().putString(Vals.TOKEN, "");
                TinyDB.getInstance().putString(Vals.USER_TYPE, "");
//                mSocket.disconnect();
//                mSocket.off("location_receive", onNewMessage);
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                        .replace(R.id.fl_signup_container, new LoginFragment())
                        .commitAllowingStateLoss();
            }
        });
       /* mSocket.on("location_receive", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
//JSON Format will be received

                Log.i("location_receive", data.toString());
                // Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
            }
        });*/
        return view;
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("Mehak", "Re");
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
}