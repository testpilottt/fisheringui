package com.example.projectui;

import static com.example.projectui.service.RestApiCallServiceImpl.POST_MEMBERLOGIN;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.databinding.FragmentFirstBinding;
import com.example.projectui.enums.AccessLevel;
import com.example.projectui.service.RestApiCallServiceImpl;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Objects;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private EditText loginUsername, loginPassword;

    private Snackbar mySnackbar;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginUsername = (EditText) getView().findViewById(R.id.loginUsername);
        loginPassword = (EditText) getView().findViewById(R.id.loginPassword);
         //TODO: remove set text
        loginUsername.setText("rohan");
        loginPassword.setText("password");

        binding.buttonFirst.setOnClickListener(view12 -> {
            JSONObject returnJsonBody = loginVerification(view12);
            if (returnJsonBody != null) {
                mySnackbar.setText("Login successful!");
                mySnackbar.show();

                Bundle bundleFromLoginFragment = new Bundle();
                bundleFromLoginFragment.putLong("memberId", Long.parseLong(returnJsonBody.get("memberId").toString()));
                getParentFragmentManager().setFragmentResult("bundleFromLoginFragment", bundleFromLoginFragment);

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.buttonAdminlogin.setOnClickListener(view1 -> {
            JSONObject returnJsonBody = loginVerification(view1);
            if (returnJsonBody != null) {

                if (AccessLevel.valueOf(returnJsonBody.get("accessLevel").toString()).equals(AccessLevel.ADMIN)) {
                    mySnackbar.setText("Admin login successful!");
                    mySnackbar.show();

                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                } else {
                    mySnackbar.setText("You don't have admin access!");
                    mySnackbar.show();
                }
            }
        });
    }

    private JSONObject loginVerification(View view) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", loginUsername.getText().toString());
        jsonObject.put("password", loginPassword.getText().toString());
        RestApiCallServiceImpl restApiCallService = new RestApiCallServiceImpl();
        JSONObject returnJsonObject = restApiCallService.sendPostRequest(POST_MEMBERLOGIN, jsonObject);
        mySnackbar = Snackbar.make(view, "Login failed, please try again.", BaseTransientBottomBar.LENGTH_LONG);

        if (Objects.isNull(returnJsonObject)) {
            mySnackbar.setText("Unexpected error, check network and try again!");
            mySnackbar.show();
            return null;
        } else {
            String httpResponseCode = returnJsonObject.get("code").toString();

            if (httpResponseCode.equals("401")) {
                mySnackbar.show();
            } else if (httpResponseCode.equals("404")) {
                mySnackbar.setText("Unexpected error, check network and try again!");
                mySnackbar.show();
            } else if (httpResponseCode.equals("202")) {

                JSONParser parser = new JSONParser();
                JSONObject returnedJsonObject;

                try {
                    returnedJsonObject = (JSONObject) parser.parse(returnJsonObject.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                return returnedJsonObject;
            }
        }
        return null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}