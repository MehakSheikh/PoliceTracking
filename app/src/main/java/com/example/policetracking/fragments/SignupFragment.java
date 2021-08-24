package com.example.policetracking.fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.policetracking.R;
import com.example.policetracking.network.ServerRequests;
import com.example.policetracking.utils.NetworkConnection;
import com.example.policetracking.utils.Utils;
import com.example.policetracking.viewmodels.LoginRequest;
import com.example.policetracking.viewmodels.LoginResponse;
import com.example.policetracking.viewmodels.RegisterUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFragment extends CoreFragment implements OnClickListener {
    private static View view;
    private static EditText firstName, cnic, mobileNumber, branch,
            password, confirmPassword, fatherName;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    private static FragmentManager fragmentManager;

    public SignupFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();

        firstName = (EditText) view.findViewById(R.id.firstName);
        fatherName = (EditText) view.findViewById(R.id.fatherName);
        cnic = (EditText) view.findViewById(R.id.cnic);
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
        branch = (EditText) view.findViewById(R.id.branch);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        login = (TextView) view.findViewById(R.id.already_user);
        terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

        /*  // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }*/
        cnic.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = cnic.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String str = s.toString();

                    String val = cnic.getText().toString();
                    if ((val.length() == 5 && len < val.length()) || (val.length() == 13 && len < val.length())) {
                        str += "-";
                        cnic.setText(str);
                        cnic.setSelection(str.length());
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:
                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:
                // Replace login fragment

                fragmentManager.beginTransaction()
                        .replace(R.id.fl_signup_container, new LoginFragment(),
                                Utils.Login_Fragment).commit();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {
        // Get all edittext texts
        String getfirstName = firstName.getText().toString();
        String getfatherName = fatherName.getText().toString();
        String getCNIC = cnic.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getbranch = branch.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();
        String rank = "";
        String buckleNum = "";

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        // Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getfirstName.equals("") || getfirstName.length() == 0
                || getCNIC.equals("") || getCNIC.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getbranch.equals("") || getbranch.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)
            Toast.makeText(getContext(), " All fields are required.", Toast.LENGTH_SHORT).show();
            // Check if email id valid or not
//        else if (!m.find())
//            Toast.makeText(getContext(), "Your Email Id is Invalid.", Toast.LENGTH_SHORT).show();
            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            Toast.makeText(getContext(), "\"Both password doesn't match.", Toast.LENGTH_SHORT).show();
            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            Toast.makeText(getContext(), "  \"Please select Terms and Conditions.", Toast.LENGTH_SHORT).show();
            // Else do signup or do your stuff
        else {
            registerUser(getfirstName, getfatherName, getCNIC, getMobileNumber, getbranch, rank, buckleNum, getPassword);
            Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void registerUser(String fname, String father_name, String CNIC, String mblNum, String branch, String rank, String buckleNum, String pwd) {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setName(fname);
        registerUser.setFatherName(father_name);
        registerUser.setBuckleNumber(buckleNum);
        registerUser.setRankId(rank);
        registerUser.setCnic(CNIC);
        registerUser.setBranchId(branch);
        registerUser.setContact(mblNum);
        registerUser.setPassword(pwd);

        final Call<RegisterUser> loginRequest = ServerRequests.getInstance(getContext()).registerUser(registerUser);
        if (NetworkConnection.isOnline(getContext())) {
            loginRequest.enqueue(new Callback<RegisterUser>() {
                @Override
                public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                    if (response.isSuccessful()) {
                        /*if (response.body().getUserType() == 1) {
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
                        }*/
                    }
                }

                @Override
                public void onFailure(Call<RegisterUser> call, Throwable t) {
                }
            });
        } else {
            Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_LONG);
        }
    }
}