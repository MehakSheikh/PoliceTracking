package com.example.policetracking.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import androidx.fragment.app.Fragment;

import com.example.policetracking.MyFragmentManager;
public class CoreFragment extends Fragment {

    protected MyFragmentManager mFragmentManager;
    protected String className;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFragmentManager = (MyFragmentManager) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        className = getClass().getName();

        if (getArguments() != null) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentManager = null;
    }

    public void hideKeyboard() {
        try {
            InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    public void showKeyboard(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

   /* public void replaceFragment(Fragment f, boolean bStack, boolean withTransition) {
        try {
            mFragmentManager.replaceFragment(f, bStack, withTransition);
        } catch (Exception e) {
        }
    }*/

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void changeTitle(String t) {
        try {
            mFragmentManager.changeTitle(t);
        } catch (Exception e) {
        }
    }
}
