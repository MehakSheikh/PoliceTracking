package com.example.policetracking.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.example.policetracking.R;
import com.example.policetracking.activities.LoginActivity;
import com.example.policetracking.activities.MainActivity;
import com.example.policetracking.utils.TinyDB;
import com.example.policetracking.utils.Utils;
import com.example.policetracking.network.ServerRequests;
import com.example.policetracking.utils.NetworkConnection;
import com.example.policetracking.utils.Vals;
import com.example.policetracking.viewmodels.LatLongRequest;
import com.example.policetracking.viewmodels.LoginRequest;
import com.example.policetracking.viewmodels.LoginResponse;
import com.example.policetracking.viewmodels.locationGet.BranchesResponseModel;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.ContextCompat.getSystemService;

public class LoginFragment extends CoreFragment implements OnClickListener {

    RelativeLayout rootLayout;
    private static EditText et_cnic, password;
    private static Button loginButton;
    private static TextView forgotPassword;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    ProgressBar progress;
    RelativeLayout rl_progress_bar;
    AlertDialog alertDialog;

    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        rootLayout = (RelativeLayout) inflater.inflate(R.layout.login_layout, container, false);
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initViews(view);
        setListeners();
        return view;
    }

    // Initiate Views
    private void initViews(View view) {
        fragmentManager = getActivity().getSupportFragmentManager();
//        ConnectivityManager conMan = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//           NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();
        WifiManager wifi = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()) {
            Toast.makeText(getContext(), "WIFI is enabled", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(getContext(), "WIFI is disabled", Toast.LENGTH_LONG);
        }
        if (isMobileDataEnabled()) {
            Toast.makeText(getContext(), "MOBILE DATA is enabled", Toast.LENGTH_LONG);
        } else
            Toast.makeText(getContext(), "MOBILE DATA is disabled", Toast.LENGTH_LONG);

        et_cnic = (EditText) view.findViewById(R.id.et_cnic);
        password = (EditText) view.findViewById(R.id.login_password);
        loginButton = (Button) view.findViewById(R.id.loginBtn);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
        // signUp = (TextView) view.findViewById(R.id.createAccount);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        rl_progress_bar = (RelativeLayout) view.findViewById(R.id.rl_progress_bar);
        show_hide_password = (CheckBox) view
                .findViewById(R.id.show_hide_password);
        loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
        et_cnic.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = et_cnic.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String str = s.toString();

                    String val = et_cnic.getText().toString();
                    if ((val.length() == 5 && len < val.length()) || (val.length() == 13 && len < val.length())) {
                        str += "-";
                        et_cnic.setText(str);
                        et_cnic.setSelection(str.length());
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
     /*   // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }*/

    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        // signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;

    /*        case R.id.forgot_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        //  .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.fl_signup_container,
                                new ForgotPassword_Fragment(),
                                Utils.ForgotPassword_Fragment).commit();
                break;*/
       /*     case R.id.createAccount:
                // Replace signup frgament with animation
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                        .replace(R.id.fl_signup_container, new SignupFragment())
                        .addToBackStack(new SignupFragment().getClass().getSimpleName())
                        .commitAllowingStateLoss();
            *//*    fragmentManager
                        .beginTransaction()
                        //  .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.fl_signup_container, new SignupFragment(),
                                Utils.SignUp_Fragment).commit();*//*
                break;*/
        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        String cnic = et_cnic.getText().toString();
        String pwd = password.getText().toString();

        // Check patter for email id
   /*     Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(cnic);
*/
        // Check for both field is empty or not
        if ((cnic.equals("") || cnic.length() == 0)
                && (pwd.equals("") || pwd.length() == 0)) {
            alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                    .setTitle("Enter CNIC and Password")
                    //  .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(null, null).show();
        } else if (cnic.equals("") || cnic.length() == 0) {
            alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                    .setTitle("Enter CNIC")
                    //  .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(null, null).show();
        } else if (pwd.equals("") || pwd.length() == 0) {
            alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                    .setTitle("Enter Password")
                    //  .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(null, null).show();
        } else {
       /*     fragmentManager
                    .beginTransaction()
                    //  .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.fl_signup_container, new HomeFragment(),
                            Utils.Home_Fragment).commit();*/

            LoginUser(cnic, pwd);
//            Toast.makeText(getActivity(), "Login Started.", Toast.LENGTH_SHORT)
//                    .show();
        }
    }

    public void LoginUser(String cnic, String pwd) {
        rl_progress_bar.setClickable(true);
        progress.setVisibility(View.VISIBLE);
        LoginRequest loginReq = new LoginRequest();
        loginReq.setCnic(cnic);
        loginReq.setPassword(pwd);
        final Call<LoginResponse> loginRequest = ServerRequests.getInstance(getContext()).loginUser(loginReq);
        if (NetworkConnection.isOnline(getContext())) {
            loginRequest.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        rl_progress_bar.setClickable(false);
                        progress.setVisibility(View.GONE);
                        if (!response.body().getJwt().equals("")) {
                            TinyDB.getInstance().putString(Vals.TOKEN, "");
                            TinyDB.getInstance().putString(Vals.TOKEN, response.body().getJwt());
                            TinyDB.getInstance().putString(Vals.USER_TYPE, response.body().getRole());
                            if (response.body().getRole().equals("ADMIN")) {
                                fragmentManager.beginTransaction()
                                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                                        .replace(R.id.fl_signup_container, new AdminMenuFragment())
                                        //  .addToBackStack(new AdminMenuFragment().getClass().getSimpleName())
                                        .commitAllowingStateLoss();
                            } else {
                                fragmentManager.beginTransaction()
                                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                                        .replace(R.id.fl_signup_container, new HomeFragment())
                                        //    .addToBackStack(new HomeFragment().getClass().getSimpleName())
                                        .commitAllowingStateLoss();
                            }
                        }
                    } else {
                        rl_progress_bar.setClickable(false);
                        progress.setVisibility(View.GONE);
                        //   Toast.makeText(getContext(), "Invalid Credentials", Toast.LENGTH_LONG);
                        alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                                .setTitle("Invalid Credentials")
                                //  .setMessage("Are you sure you want to exit?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton(null, null).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    rl_progress_bar.setClickable(false);
                    progress.setVisibility(View.GONE);
                }
            });
        } else {
            rl_progress_bar.setClickable(false);
            progress.setVisibility(View.GONE);
            alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                    .setTitle("Check your Internet Connection")
                    //  .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(null, null).show();
//            Toast.makeText(getActivity(), "Login Started.", Toast.LENGTH_SHORT).show();
        }
    }

    public void receiveLocation(String jwt) {
        LatLongRequest latLongRequest = new LatLongRequest();
        latLongRequest.setJwt(jwt);

        final Call<BranchesResponseModel> loginRequest = ServerRequests.getInstance(getContext()).recLatLong(2);
        if (NetworkConnection.isOnline(getContext())) {
            loginRequest.enqueue(new Callback<BranchesResponseModel>() {
                @Override
                public void onResponse(Call<BranchesResponseModel> call, Response<BranchesResponseModel> response) {
                    if (response.isSuccessful()) {
                        Calendar calendar = Calendar.getInstance(Locale.getDefault());
                        int offset = -(calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000);
                   /*     Double latitude = Double.valueOf(response.body().getData().getLocation().getLat());
                        Double lng = Double.valueOf(response.body().getData().getLocation().getLng());
                        String dateString = response.body().getData().getLocation().getUpdatedAt();
                        String s1= 2021-09-20 05:21:31"";
                        String[] words=dateString.split("\\+");//splits the string based on whitespace
                        dateFormat(words[0]);*/
                        if (response.body().getData().getLocation() != null && response.body().getData().getLocation().getUpdatedAt() != null && !response.body().getData().getLocation().getUpdatedAt().equals("")) {
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                                    .setTitle("User not updated location")
                                    //  .setMessage("Are you sure you want to exit?")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).setNegativeButton(null, null).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BranchesResponseModel> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_LONG);
        }
    }

    private boolean isMobileDataEnabled() {
        boolean mobileDataEnabled = false;
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);

            mobileDataEnabled = (Boolean) method.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileDataEnabled;
    }

    public void dateFormat(String dateString) {
        // Get Current Date Time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm aa");
        String getCurrentDateTime = sdf.format(c.getTime());

        Log.d("getCurrentDateTime", getCurrentDateTime);
// getCurrentDateTime: 05/23/2016 18:49 PM
     /*   long diff = 0;
        int day = 0;
        int min = 0;
        if (diff / 86400 > 0) {
            day = (int) (diff / 86400);
            min = day / 60;
        }*/

        DateTimeFormatter inputFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm aa", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(dateString, inputFormatter);
            String formattedDate = outputFormatter.format(date);
            System.out.println(formattedDate); // prints 10-04-2018
        }

        if (getCurrentDateTime.compareTo(dateString) < 0) {

        } else {
            Log.d("Return", "getMyTime older than getCurrentDateTime ");
        }
    }
}