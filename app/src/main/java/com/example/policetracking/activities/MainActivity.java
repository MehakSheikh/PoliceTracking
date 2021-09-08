package com.example.policetracking.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.policetracking.R;
import com.example.policetracking.activities.LoginActivity;
import com.example.policetracking.utils.TinyDB;
import com.example.policetracking.utils.Vals;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {

    private SupportMapFragment mapFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background_color));
        }
/*      if (!TinyDB.getInstance().getString(Vals.TOKEN).equals("")) {
            if (!TinyDB.getInstance().getString(Vals.USER_TYPE).equals("") && TinyDB.getInstance().getString(Vals.USER_TYPE).equals("admin")) {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
            }
            else{
                Intent i = new Intent(this, HomeConstableActivity.class);
                startActivity(i);
                finish();
            }
        }*/
        Intent i = new Intent(this, HomeConstableActivity.class);
        startActivity(i);
        finish();
    }

    /*public void removeFrag() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.getBackStackEntryCount();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
    }*/
}