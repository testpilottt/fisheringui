package com.example.projectui;

import static com.example.projectui.MainActivity.POST_MEMBERLOGIN;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.databinding.FragmentFirstBinding;
import com.example.projectui.service.restApiCallServiceImpl;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.simple.JSONObject;

import java.util.Objects;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    EditText loginUsername, loginPassword;


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

        restApiCallServiceImpl restApiCallService = new restApiCallServiceImpl();


        loginUsername = (EditText) getView().findViewById(R.id.loginUsername);
        loginPassword = (EditText) getView().findViewById(R.id.loginPassword);
         //TODO: remove set text
        loginUsername.setText("rohan");
        loginPassword.setText("password");

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", loginUsername.getText().toString());
                jsonObject.put("password", loginPassword.getText().toString());

                JSONObject returnJsonObject = restApiCallService.sendPostRequest(POST_MEMBERLOGIN, jsonObject);
                Snackbar mySnackbar = Snackbar.make(view, "Login failed, please try again.", BaseTransientBottomBar.LENGTH_LONG);

                if (Objects.isNull(returnJsonObject)) {
                    mySnackbar = Snackbar.make(view, "Unexpected error, check network and try again!", BaseTransientBottomBar.LENGTH_LONG);
                    mySnackbar.show();
                } else {
                    String httpResponseCode = returnJsonObject.get("code").toString();

                    if (httpResponseCode.equals("401")) {
                        mySnackbar.show();
                    } else if (httpResponseCode.equals("202")) {
                        mySnackbar = Snackbar.make(view, "Login successful!", BaseTransientBottomBar.LENGTH_LONG);
                        mySnackbar.show();

                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}