package com.example.policetracking.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.policetracking.R;
import com.example.policetracking.adapters.NothingSelectedSpinnerAdapter;
import com.example.policetracking.network.ServerRequests;
import com.example.policetracking.utils.NetworkConnection;
import com.example.policetracking.utils.Utils;
import com.example.policetracking.viewmodels.BranchesResponseModel;
import com.example.policetracking.viewmodels.RanksResponseModel;
import com.example.policetracking.viewmodels.RegisterUser;
import com.example.policetracking.viewmodels.UserListing.UserListingModel;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFragment extends CoreFragment implements OnClickListener, AdapterView.OnItemSelectedListener {
    private static View view;
    private static EditText firstName, cnic, branch, mobileNumber,
            password, confirmPassword, fatherName, buckleNumber;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    ProgressBar progress;
    RelativeLayout rl_progress_bar;
    private static FragmentManager fragmentManager;
    private static String[] arr_ranks;
    Spinner spBranch, spRanks;
    List<String> rank_list = new ArrayList<String>();
    List<String> branch_list = new ArrayList<String>();
    String getfirstName, getfatherName, getCNIC, getMobileNumber, buckleNum, getPassword, getConfirmPassword;
    String getbranch, rank;
    private static String[] paths = {"1", "2", "3"};
    private Handler queueHandler;

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
        spBranch = (Spinner) view.findViewById(R.id.spBranch);
        spRanks = (Spinner) view.findViewById(R.id.spRanks);
        buckleNumber = (EditText) view.findViewById(R.id.buckleNumber);
        // branch = (EditText) view.findViewById(R.id.branch);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        //     login = (TextView) view.findViewById(R.id.already_user);
        //   terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        rl_progress_bar = (RelativeLayout) view.findViewById(R.id.rl_progress_bar);

        getRanks();
        getBranches();
        queueHandler = new Handler(Looper.getMainLooper());
        queueHandler.postDelayed(() -> {
            ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, branch_list);

            branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spBranch.setAdapter(branchAdapter);
            spBranch.setOnItemSelectedListener(this);
            spBranch.setPrompt("Select Branch");

            spBranch.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            branchAdapter,
                            R.layout.branch_spinner_row_nothing_selected,
                            getContext()));

            ArrayAdapter<String> rankAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, rank_list);

            rankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRanks.setAdapter(rankAdapter);
            spRanks.setOnItemSelectedListener(this);
            spRanks.setPrompt("Select Rank");

            spRanks.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            rankAdapter,
                            R.layout.ranks_spinner_row_nothing_selected,
                            getContext()));
            //  progress.setClickable(false);
            rl_progress_bar.setClickable(false);
            progress.setVisibility(View.GONE);
        }, 10000);


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
        //  login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:
                // Call checkValidation method
                checkValidation();
                break;

          /*  case R.id.already_user:
                // Replace login fragment
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                        .replace(R.id.fl_signup_container, new LoginFragment())
                        .addToBackStack(new LoginFragment().getClass().getSimpleName())
                        .commitAllowingStateLoss();
                break;*/
        }

    }

    // Check Validation Method
    private void checkValidation() {
        // Get all edittext texts
        getfirstName = firstName.getText().toString();
        getfatherName = fatherName.getText().toString();
        getCNIC = cnic.getText().toString();
        getMobileNumber = mobileNumber.getText().toString();
        if (spBranch.getSelectedItemId() != -1) {
            getbranch = String.valueOf(spBranch.getSelectedItemId());
        }
        if (spRanks.getSelectedItemId() != -1) {
            rank = String.valueOf(spRanks.getSelectedItemId());
        }
        getPassword = password.getText().toString();
        getConfirmPassword = confirmPassword.getText().toString();
        buckleNum = buckleNumber.getText().toString();

        // Pattern match for email id
        // Pattern p = Pattern.compile(Utils.regEx);
        // Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getfirstName.equals("") || getfirstName.length() == 0 ||
                getfatherName.equals("") || getfatherName.length() == 0
                || getCNIC.equals("") || getCNIC.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                    /* || getbranch.equals("") || getbranch.length() == 0
                     || getRanks().equals("") || getRanks().length() == 0*/
                || buckleNum.equals("") || buckleNum.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0 || spBranch.getSelectedItemId() == -1 || spRanks.getSelectedItemId() == -1) {
            Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
        } else if (getPassword.length() < 6) {
            Toast.makeText(getContext(), "\"Password should be 6 characters long.", Toast.LENGTH_SHORT).show();
        }
        // Check if email id valid or not
//        else if (!m.find())
//            Toast.makeText(getContext(), "Your Email Id is Invalid.", Toast.LENGTH_SHORT).show();
        // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            Toast.makeText(getContext(), "Both password doesn't match.", Toast.LENGTH_SHORT).show();
            // Make sure user should check Terms and Conditions checkbox
//        else if (!terms_conditions.isChecked())
//            Toast.makeText(getContext(), "  \"Please select Terms and Conditions.", Toast.LENGTH_SHORT).show();
            // Else do signup or do your stuff
        else {
            registerUser(getfirstName, getfatherName, getCNIC, getMobileNumber, getbranch, rank, buckleNum, getPassword);
         /*   Toast.makeText(getActivity(), "Signup Started.", Toast.LENGTH_SHORT)
                    .show();*/
        }
    }

    public void registerUser(String fname, String father_name, String CNIC, String mblNum, String branch, String rank, String buckleNum, String pwd) {
        RegisterUser registerUser = new RegisterUser();

        registerUser.setName(fname);
        registerUser.setFatherName(father_name);
        registerUser.setCnic(CNIC);
        registerUser.setContact(mblNum);
        registerUser.setBranchId(branch);
        registerUser.setRankId(rank);
        registerUser.setBuckleNumber(buckleNum);
        registerUser.setPassword(pwd);

       /* registerUser.setName("fname");
        registerUser.setFatherName("father name");
        registerUser.setBuckleNumber("123");
        registerUser.setRankId(rank);
        registerUser.setCnic(CNIC);
        registerUser.setBranchId(branch);
        registerUser.setContact("03333009977");
        registerUser.setPassword("111");*/

        final Call<RegisterUser> loginRequest = ServerRequests.getInstance(getContext()).registerUser(registerUser);
        if (NetworkConnection.isOnline(getContext())) {
            loginRequest.enqueue(new Callback<RegisterUser>() {
                @Override
                public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                    if (response.isSuccessful()) {

                        Toast.makeText(getActivity(), "Register User Successfully", Toast.LENGTH_SHORT).show();
                        //     getActivity().getFragmentManager().popBackStack();
                    /*    fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                                .replace(R.id.fl_signup_container, new AdminMenuFragment())
                                //  .addToBackStack(new AdminMenuFragment().getClass().getSimpleName())
                                .commitAllowingStateLoss();*/
                    /*    FragmentManager manager = getActivity().getSupportFragmentManager();
                        manager.getBackStackEntryCount();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.remove(new SignupFragment())nad;
                        trans.commit();*/
                    } else {
                        if (response.code() == 400) {
                            String message = "Please enter a different CNIC and Try Again";

                            //   message = response.body().getErrors().getCnic().toString();
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                                        .setTitle("CNIC already exist")
                                    .setMessage(message)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).setNegativeButton(null, null).show();

                        }
                      /*  try {
                            JSONObject jObjError = new JSONObject((Map) response.errorBody());
                            if (jObjError.has("errors")) {

                                //  Vals.serverErrorDialog(getContext(), jObjError.getString("Message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Vals.serverErrorDialog(getContext(), "Something went wrong");
                        }
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                                .setTitle("Something went wrong")
                                .setMessage("Try Again")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton(null, null).show();*/
                    }
                }


                @Override
                public void onFailure(Call<RegisterUser> call, Throwable t) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                            .setTitle("Something went wrong")
                            //  .setMessage("Are you sure you want to exit?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton(null, null).show();
                }
            });
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                    .setTitle("Check your Internet Connection")
                    //  .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(null, null).show();
        }
    }

    public void getRanks() {
        //   progress.setVisibility(View.VISIBLE);
        final Call<RanksResponseModel> ranks = ServerRequests.getInstance(getContext()).getRanks();
        if (NetworkConnection.isOnline(getContext())) {
            ranks.enqueue(new Callback<RanksResponseModel>() {
                @Override
                public void onResponse(Call<RanksResponseModel> call, Response<RanksResponseModel> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            rank_list.add(response.body().getData().get(i).getName());
                        }
                        Toast.makeText(getActivity(), "Got all Ranks Successfully", Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<RanksResponseModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Failure", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_LONG).show();
        }
    }


    public void getBranches() {
        //       progress.setVisibility(View.VISIBLE);
        final Call<RanksResponseModel> branches = ServerRequests.getInstance(getContext()).getBranches();
        if (NetworkConnection.isOnline(getContext())) {
            branches.enqueue(new Callback<RanksResponseModel>() {
                @Override
                public void onResponse(Call<RanksResponseModel> call, Response<RanksResponseModel> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            branch_list.add(response.body().getData().get(i).getName());
                        }
                        Toast.makeText(getActivity(), "Got all Branches Successfully", Toast.LENGTH_SHORT)
                                .show();

                    }
                }

                @Override
                public void onFailure(Call<RanksResponseModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Failure", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}