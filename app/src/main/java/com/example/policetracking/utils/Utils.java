package com.example.policetracking.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.policetracking.R;

public class Utils {
    //Email Validation pattern
    public static final String regEx = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //Fragments Tags
    public static final String Login_Fragment = "LoginFragment";
    public static final String SignUp_Fragment = "SignUp_Fragment";
    public static final String ForgotPassword_Fragment = "ForgotPassword_Fragment";
  public static final String Home_Fragment = "HomeFragment";
  public static final String User_Listing_Fragment = "UserListingFragment";
  public static final String Maps_Fragment = "MapsFragment";


}
