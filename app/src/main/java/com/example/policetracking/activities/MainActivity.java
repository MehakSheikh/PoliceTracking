package com.example.policetracking.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.policetracking.R;
import com.example.policetracking.activities.LoginActivity;
import com.example.policetracking.fragments.AdminMenuFragment;
import com.example.policetracking.fragments.HomeFragment;
import com.example.policetracking.utils.TinyDB;
import com.example.policetracking.utils.Vals;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {

    private SupportMapFragment mapFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TinyDB.dbContext = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background_color));
        }
        if (!TinyDB.getInstance().getString(Vals.TOKEN).equals("")) {
            if (!TinyDB.getInstance().getString(Vals.USER_TYPE).equalsIgnoreCase("") && TinyDB.getInstance().getString(Vals.USER_TYPE).equalsIgnoreCase("admin")) {
                replaceFragment(new AdminMenuFragment(), false, false);
            } else {
//                Intent serviceIntent = new Intent(this, ExampleService.class);
//                serviceIntent.putExtra("inputExtra", "Your location is sharing");
//                ContextCompat.startForegroundService(this, serviceIntent);
                replaceFragment(new HomeFragment(), false, false);
            }
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
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
    public void startService(View v) {
        String input = "Notify";
        Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("inputExtra", input);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
    }
}