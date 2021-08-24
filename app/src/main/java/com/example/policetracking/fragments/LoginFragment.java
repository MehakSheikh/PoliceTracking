package com.example.policetracking.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.policetracking.utils.Utils;
import com.example.policetracking.network.ServerRequests;
import com.example.policetracking.utils.NetworkConnection;
import com.example.policetracking.viewmodels.LoginRequest;
import com.example.policetracking.viewmodels.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends CoreFragment implements OnClickListener {

    RelativeLayout rootLayout;
    private static EditText et_cnic, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;

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

        et_cnic = (EditText) view.findViewById(R.id.et_cnic);
        password = (EditText) view.findViewById(R.id.login_password);
        loginButton = (Button) view.findViewById(R.id.loginBtn);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
        signUp = (TextView) view.findViewById(R.id.createAccount);
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
        signUp.setOnClickListener(this);

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

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        //  .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.fl_signup_container,
                                new ForgotPassword_Fragment(),
                                Utils.ForgotPassword_Fragment).commit();
                break;
            case R.id.createAccount:
                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        //  .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.fl_signup_container, new SignupFragment(),
                                Utils.SignUp_Fragment).commit();
                break;
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
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("Enter CNIC and Password")
                    //  .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(null, null).show();
        }else if (cnic.equals("") || cnic.length() == 0){
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("Enter CNIC")
                    //  .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(null, null).show();
        }else if(pwd.equals("") || pwd.length() == 0){
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("Enter Password")
                    //  .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(null, null).show();
        }

        else {
            LoginUser(cnic,pwd);
            Toast.makeText(getActivity(), "Login.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void LoginUser(String email, String pwd) {
        LoginRequest loginReq = new LoginRequest();
        loginReq.setEmail(email);
        loginReq.setPassword(pwd);
        final Call<LoginResponse> loginRequest = ServerRequests.getInstance(getContext()).loginUser(loginReq);
        if (NetworkConnection.isOnline(getContext())) {
            loginRequest.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getUserType() == 1) {
                            fragmentManager
                                    .beginTransaction()
                                    //  .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                                    .replace(R.id.fl_signup_container, UserListingFragment.instance(),
                                            Utils.User_Listing_Fragment).commit();
                        } else {
                            fragmentManager
                                    .beginTransaction()
                                    //  .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                                    .replace(R.id.fl_signup_container, new HomeFragment(),
                                            Utils.Home_Fragment).commit();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                }
            });
        } else {
            Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_LONG);
        }
    }
}