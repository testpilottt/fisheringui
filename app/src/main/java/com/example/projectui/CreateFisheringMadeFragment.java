package com.example.projectui;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static com.example.projectui.Helper.RESTApiRequestURL.GET_TYPEOFFISHLIST;
import static com.example.projectui.Helper.RESTApiRequestURL.POST_CREATENEWFISHERINGMADE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.ListAdapter.TypeOfFishSpinnerAdapter;
import com.example.projectui.databinding.CreatefishringmadeFragmentBinding;
import com.example.projectui.dto.TypeOfFish;
import com.example.projectui.enums.Country;
import com.example.projectui.service.LocationServiceImpl;
import com.example.projectui.service.RestApiCallServiceImpl;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.chromium.base.Promise;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CreateFisheringMadeFragment extends Fragment {
    private Snackbar mySnackbar;
    private CreatefishringmadeFragmentBinding binding;

    private Context currentActivity;

    private Context currentContext;

    private Consumer<Location> getLocationConsumer;

    private Intent intentPictureData;

    private Country currentCountry;

    private TypeOfFish currentSelectedTypeOfFish;

    private String currentLocationString;

    private Long currentMemberId;

    Bundle bundleFromCreateFisheringMadeFragment = new Bundle();
    RestApiCallServiceImpl restApiCallService = new RestApiCallServiceImpl();

    public void setGetLocationConsumer(Consumer<Location> getLocationConsumer) {
        this.getLocationConsumer = getLocationConsumer;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = CreatefishringmadeFragmentBinding.inflate(inflater, container, false);
        this.currentActivity = getActivity();
        this.currentContext = this.currentActivity.getApplicationContext();
        binding.inputLocation.setEnabled(false);

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

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetLocationConsumer(this::onLocationConsumerRetrieve);

        bundlePromiseMethod().then(v1 -> {
            initTypeOfFishSpinner(view);
            binding.buttonGetLocation.setOnClickListener(v -> {
                LocationServiceImpl locationService = new LocationServiceImpl();
                locationService.getLocation(getActivity(), getLocationConsumer);
            });

            binding.buttonRegistercatch.setOnClickListener(v -> {
                if (validationCheck(view)) {
                    restApiCallService.sendPostRequest(POST_CREATENEWFISHERINGMADE, collectData()).then(returnJsonObject -> {
                        if (Objects.isNull(returnJsonObject)) {
                            mySnackbar = Snackbar.make(view, "", BaseTransientBottomBar.LENGTH_LONG);
                            mySnackbar.setText("Your country has not been configured. Please contact administrator.");
                            mySnackbar.show();
                        }
                        String httpResponseCode = returnJsonObject.get("code").toString();
                        if ("404".equals(httpResponseCode)) {
                            mySnackbar = Snackbar.make(view, "", BaseTransientBottomBar.LENGTH_LONG);
                            mySnackbar.setText("Your country has not been configured. Please contact administrator.");
                            mySnackbar.show();
                        }  else if ("409".equals(httpResponseCode)) {
                            mySnackbar = Snackbar.make(view, "", BaseTransientBottomBar.LENGTH_LONG);
                            mySnackbar.setText("Blockchain has been tampered with, try again.");
                            mySnackbar.show();
                        }else if ("201".equals(httpResponseCode)) {
                            bundleFromCreateFisheringMadeFragment.putString("country", currentCountry.name());
                            bundleFromCreateFisheringMadeFragment.putLong("memberId", currentMemberId);

                            getParentFragmentManager().setFragmentResult("bundleFromCreateFisheringMadeFragment", bundleFromCreateFisheringMadeFragment);

                            NavHostFragment.findNavController(CreateFisheringMadeFragment.this)
                                    .navigate(R.id.action_CreateFisheringMadeFragment_to_FisheringMadeFragment);
                        }
                    });
                }
            });
            binding.buttonBack.setOnClickListener(v -> {
                NavHostFragment.findNavController(CreateFisheringMadeFragment.this)
                        .navigate(R.id.action_CreateFisheringMadeFragment_to_FisheringMadeFragment);

                bundleFromCreateFisheringMadeFragment.putString("country", currentCountry.name());
                bundleFromCreateFisheringMadeFragment.putLong("memberId", currentMemberId);

                getParentFragmentManager().setFragmentResult("bundleFromCreateFisheringMadeFragment", bundleFromCreateFisheringMadeFragment);

            });
        });
    }

    private Promise<Void> bundlePromiseMethod() {
        Promise<Void> promise = new Promise<>();
        getParentFragmentManager().setFragmentResultListener("bundleFromFisheringMadeFragment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                currentCountry = Country.valueOf(result.getString("country"));
                currentMemberId = result.getLong("memberId");
                promise.fulfill(null);
            }
        });

        return promise;
    }

    private boolean validationCheck(View view) {
        boolean isValid = true;
        mySnackbar = Snackbar.make(view, "", BaseTransientBottomBar.LENGTH_LONG);
        if (Objects.isNull(binding.imageView.getDrawable())) {
            mySnackbar.setText("Please add an Image of the fish first!");

            mySnackbar.show();
            isValid = false;
        } else if (binding.inputLocation.getText().toString().isEmpty() || currentLocationString.isEmpty()) {
            mySnackbar.setText("Please add the location!");
            mySnackbar.show();
            isValid = false;
        } else if (binding.inputWeightkg.getText().toString().isEmpty()) {
            mySnackbar.setText("Please add the weight!");
            mySnackbar.show();
            isValid = false;
        }

        return isValid;
    }

    private void onLocationConsumerRetrieve(Location location) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Longitude = ").append(location.getLongitude());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Latitude = ").append(location.getLatitude());
        currentLocationString = location.getLatitude() + "," + location.getLongitude();

        binding.inputLocation.setText(stringBuilder.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private JSONObject collectData() {

        Bitmap bm = ((BitmapDrawable)binding.imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        String weight = binding.inputWeightkg.getText().toString();
        String countryString = currentCountry.name();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("location", currentLocationString);
        jsonObject.put("weightKg", weight);
        jsonObject.put("country", countryString);
        jsonObject.put("memberId", currentMemberId);
        jsonObject.put("typeOfFishId", currentSelectedTypeOfFish.getTypeOfFishId());

        String encodedString = Base64.getEncoder().encodeToString(imageInByte);
        jsonObject.put("pictureOfFish", encodedString);

        String data = currentLocationString + weight + countryString + currentMemberId + currentSelectedTypeOfFish.getTypeOfFishId() + encodedString;
        jsonObject.put("hash", calculateHash(data));

        return jsonObject;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initTypeOfFishSpinner(View view) {
        RestApiCallServiceImpl restApiCallService = new RestApiCallServiceImpl();
        List<TypeOfFish> typeOfFishList = new ArrayList<>();
        mySnackbar = Snackbar.make(view, "", BaseTransientBottomBar.LENGTH_LONG);
        restApiCallService.sendGetRequest(GET_TYPEOFFISHLIST, null).then(returnJsonObject -> {
                    if (Objects.isNull(returnJsonObject)) {
                        mySnackbar.setText("Unexpected error, check network and try again!");
                        mySnackbar.show();
                        return;
                    } else {
                        String httpResponseCode = returnJsonObject.get("code").toString();
                        if (httpResponseCode.equals("401")) {
                            mySnackbar.show();
                        } else if (httpResponseCode.equals("404")) {
                            mySnackbar.setText("Unexpected error, check network and try again!");
                            mySnackbar.show();
                        } else if (httpResponseCode.equals("200")) {

                            JSONParser parser = new JSONParser();
                            org.json.simple.JSONArray jsonArray;

                            try {
                                jsonArray = (org.json.simple.JSONArray) parser.parse(returnJsonObject.get("body").toString());

                                jsonArray.forEach(tof -> {
                                    JSONObject jsonObject = (JSONObject) tof;

                                    if (Boolean.parseBoolean(jsonObject.get("active").toString())) {
                                        TypeOfFish typeOfFish = new TypeOfFish();
                                        typeOfFish.setTypeOfFishId(Long.valueOf(jsonObject.get("typeOfFishId").toString()));
                                        typeOfFish.setActive(Boolean.valueOf(jsonObject.get("active").toString()));
                                        typeOfFish.setTypeOfFishName(jsonObject.get("typeOfFishName").toString());

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            byte[] decodedString = Base64.getDecoder().decode(jsonObject.get("typeOfFishPictureBase64").toString());
                                            typeOfFish.setTypeOfFishPictureBase64(decodedString);
                                        }
                                        typeOfFishList.add(typeOfFish);
                                    }
                                });

                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        TypeOfFishSpinnerAdapter typeOfFishSpinnerAdapter = new TypeOfFishSpinnerAdapter(currentContext, typeOfFishList);
                        binding.spinnerTypeOfFish.setAdapter(typeOfFishSpinnerAdapter);

                        binding.spinnerTypeOfFish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                currentSelectedTypeOfFish = typeOfFishList.get(i);
                                mySnackbar.setText("Type of fish: " + currentSelectedTypeOfFish.getTypeOfFishName());
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {}
                        });
        }
        });
    }

    public String calculateHash(String text) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        final StringBuilder hexString = new StringBuilder();
        final byte[] bytes = digest.digest(text.getBytes());

        for (final byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
