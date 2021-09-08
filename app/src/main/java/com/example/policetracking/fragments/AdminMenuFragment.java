package com.example.policetracking.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.policetracking.R;
import com.example.policetracking.utils.TinyDB;
import com.example.policetracking.utils.Utils;
import com.example.policetracking.utils.Vals;

public class AdminMenuFragment extends CoreFragment {

    Button signUp_form, users_list, btn_logout;

    private static FragmentManager fragmentManager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminMenuFragment() {
        // Required empty public constructor
    }

    public static AdminMenuFragment newInstance(String param1, String param2) {
        AdminMenuFragment fragment = new AdminMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                        .replace(R.id.fl_signup_container, new LoginFragment())
                        .commitAllowingStateLoss();
            }
        });
        return view;
    }
}