package com.example.policetracking.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.policetracking.MyFragmentManager;
import com.example.policetracking.R;
import com.example.policetracking.fragments.SignupFragment;

public class SignupActivity extends AppCompatActivity implements MyFragmentManager {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        replaceFragment(new SignupFragment(), true, false);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    }
}