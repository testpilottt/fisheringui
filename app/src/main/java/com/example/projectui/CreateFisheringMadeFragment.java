package com.example.projectui;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.databinding.CreatefishringmadeFragmentBinding;
import com.example.projectui.dto.FisheringMade;
import com.example.projectui.enums.Country;
import com.example.projectui.service.LocationServiceImpl;
import com.example.projectui.service.RestApiCallServiceImpl;

import java.util.Objects;
import java.util.function.Consumer;

public class CreateFisheringMadeFragment extends Fragment {

    private CreatefishringmadeFragmentBinding binding;

    private Context currentActivity;

    private Context currentContext;

    private Consumer<Location> getLocationConsumer;

    private Location currentLocation;

    private Intent intentPictureData;

    private Country currentCountry;


    public void setGetLocationConsumer(Consumer<Location> getLocationConsumer) {
        this.getLocationConsumer = getLocationConsumer;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        if (Objects.nonNull(currentCountry)) {
            getParentFragmentManager().setFragmentResultListener("bundleFromChooseCountryFragment", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    currentCountry = Country.valueOf(result.getString("country"));
                }
            });
        }

        binding = CreatefishringmadeFragmentBinding.inflate(inflater, container, false);
        this.currentActivity = getActivity();
        this.currentContext = this.currentActivity.getApplicationContext();
        binding.inputLocation.setEnabled(false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetLocationConsumer(this::onLocationConsumerRetrieve);
        RestApiCallServiceImpl restApiCallService = new RestApiCallServiceImpl();

//        restApiCallService.sendGetRequest()

        binding.buttonGetLocation.setOnClickListener(v -> {
            LocationServiceImpl locationService = new LocationServiceImpl();
            locationService.getLocation(getActivity(), getLocationConsumer);
        });

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


        binding.buttonRegistercatch.setOnClickListener(v -> {
//            restApiCallService.sendPostRequest()
            collectData();

        });

        binding.buttonBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(CreateFisheringMadeFragment.this)
                    .navigate(R.id.action_CreateFisheringMadeFragment_to_FisheringMadeFragment);
        });

    }

    private void onLocationConsumerRetrieve(Location location) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Longitude = ").append(location.getLongitude());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Latitude = ").append(location.getLatitude());
        binding.inputLocation.setText(stringBuilder.toString());
    }

    private FisheringMade collectData() {
        FisheringMade fisheringMade = new FisheringMade();
        fisheringMade.setLocation(currentLocation.getLongitude() + ";" + currentLocation.getLatitude());
        fisheringMade.setCountry(currentCountry);
//        fisheringMade.setRegion();
//        fisheringMade.setFisheringHash();
        fisheringMade.setWeightKg(Integer.parseInt(binding.inputWeightkg.getText().toString()));
//        fisheringMade.setPictureOfFish();

        return fisheringMade;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
