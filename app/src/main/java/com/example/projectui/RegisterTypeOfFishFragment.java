package com.example.projectui;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import static com.example.projectui.Helper.RESTApiRequestURL.POST_CREATETYPEOFFISH;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.databinding.RegisterTypeoffishBinding;
import com.example.projectui.service.RestApiCallServiceImpl;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.simple.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Objects;


public class RegisterTypeOfFishFragment extends Fragment {
    private RegisterTypeoffishBinding binding;

    private Intent intentPictureData;
    RestApiCallServiceImpl restApiCallService = new RestApiCallServiceImpl();

    private Snackbar mySnackbar;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = RegisterTypeoffishBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context currentContext = getActivity().getApplicationContext();
        mySnackbar = Snackbar.make(view, "", BaseTransientBottomBar.LENGTH_LONG);

        ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        intentPictureData = result.getData();
                        Uri selectedImage = intentPictureData.getData();
                        binding.imageView.setImageURI(selectedImage);
                    }
                }
        );

        binding.buttonAddPicture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
            startActivityForResult.launch(intent);
        });


        binding.buttonRegisterfish.setOnClickListener(v -> {
            if (validationCheck()) {
                JSONObject returnJsonObject = restApiCallService.sendPostRequest(POST_CREATETYPEOFFISH, collectData());
                String httpResponseCode = returnJsonObject.get("code").toString();
                if ("201".equals(httpResponseCode)) {
                    NavHostFragment.findNavController(RegisterTypeOfFishFragment.this)
                            .navigate(R.id.action__RegisterTypeOfFishFragment_to_ManageTypeOfFishFragment);
                } else if ("208".equals(httpResponseCode)){
                    mySnackbar.setText("Name of fish must be unique");
                    mySnackbar.show();
                }
            }
        });
    }
    private boolean validationCheck() {
        boolean isValid = true;
        if (Objects.isNull(binding.imageView.getDrawable())) {
            mySnackbar.setText("Please add an Image of the fish first!");
            mySnackbar.show();
            isValid = false;
        } else if (binding.inputTypeOfFishName.getText().toString().isEmpty()) {
            mySnackbar.setText("Please add the name of the fish!");
            mySnackbar.show();
            isValid = false;
        }

        return isValid;
    }
    private JSONObject collectData() {
        Bitmap bm = ((BitmapDrawable)binding.imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("typeOfFishName", binding.inputTypeOfFishName.getText().toString());
        jsonObject.put("isActive", binding.inputActiveSwitch.isChecked());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String encodedString = Base64.getEncoder().encodeToString(imageInByte);
            jsonObject.put("typeOfFishPicture", encodedString);
        }

        return jsonObject;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
