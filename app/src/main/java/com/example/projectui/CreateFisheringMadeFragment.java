package com.example.projectui;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projectui.databinding.CreatefishringmadeFragmentBinding;
import com.example.projectui.service.LocationServiceImpl;
import com.google.android.gms.location.LocationRequest;

import java.util.function.Consumer;

public class CreateFisheringMadeFragment extends Fragment {

    private CreatefishringmadeFragmentBinding binding;
    private LocationRequest locationRequest;

    private Context currentActivity;

    private Context currentContext;

    private Consumer<Location> getLocationConsumer;

    public void setGetLocationConsumer(Consumer<Location> getLocationConsumer) {
        this.getLocationConsumer = getLocationConsumer;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = CreatefishringmadeFragmentBinding.inflate(inflater, container, false);
        this.currentActivity = getActivity();
        this.currentContext = this.currentActivity.getApplicationContext();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setGetLocationConsumer(this::onLocationConsumerRetrieve);

        binding.buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationServiceImpl locationService = new LocationServiceImpl();
                locationService.getLocation(getActivity(), getLocationConsumer);
            }
        });

    }

    private void onLocationConsumerRetrieve(Location location) {
        binding.txtCountry.setText("Long=" + location.getLongitude() +"XD" + location.getLatitude());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
