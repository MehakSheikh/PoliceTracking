package com.example.policetracking.fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.policetracking.MyFragmentManager;
import com.example.policetracking.R;
import com.example.policetracking.utils.Utils;

public class ForgotPassword_Fragment extends Fragment implements
        OnClickListener {
    private static View view;
    protected MyFragmentManager mFragmentManager;
    private static EditText emailId;
    private static TextView submit, back;

    public ForgotPassword_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.forgotpassword_layout, container,
                false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize the views
    private void initViews() {
        emailId = (EditText) view.findViewById(R.id.registered_emailid);
        submit = (TextView) view.findViewById(R.id.forgot_button);
        back = (TextView) view.findViewById(R.id.backToLoginBtn);

     /*   // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            back.setTextColor(csl);
            submit.setTextColor(csl);

        } catch (Exception e) {
        }*/

    }

    // Set Listeners over buttons
    private void setListeners() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToLoginBtn:
                // Replace Login Fragment on Back Presses
                //mFragmentManager.replaceFragment(this,false,true);
              //  new MainActivity().replaceFragment(this,false,true);
                break;

            case R.id.forgot_button:
                // Call Submit button task
                submitButtonTask();
                break;

        }

    }

    private void submitButtonTask() {
        String getEmailId = emailId.getText().toString();
        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);
        // Match the pattern
        Matcher m = p.matcher(getEmailId);
        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0)
            Toast.makeText(getContext(), "Please enter your Email Id.", Toast.LENGTH_SHORT).show();
            // Check if email id is valid or not
        else if (!m.find())
            Toast.makeText(getContext(),  "Your Email Id is Invalid.", Toast.LENGTH_SHORT).show();
            // Else submit email id and fetch passwod or do your stuff
        else
            Toast.makeText(getActivity(), "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
    }
}