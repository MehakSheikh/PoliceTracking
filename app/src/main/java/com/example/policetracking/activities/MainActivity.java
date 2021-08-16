package com.example.policetracking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.policetracking.R;
import com.example.policetracking.activities.LoginActivity;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity{

    private SupportMapFragment mapFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background_color));
        }*/

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}