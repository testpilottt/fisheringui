package com.example.projectui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.databinding.FragmentSecondBinding;
import com.example.projectui.enums.Country;

import org.chromium.base.Promise;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private ListView listView;
    private Country currentSelectedCountry;

    Bundle bundleFromChooseCountryFragment = new Bundle();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context currentContext = getActivity().getApplicationContext();
        bundlePromiseMethod().then(v1 -> {
            //TODO::RAU
            List<Country> availableCountries = Arrays.asList(Country.values()).stream()
//                    .filter(country -> Country.MAURITIUS == country)
                    .collect(Collectors.toList());

            ArrayAdapter arrayAdapter = new ArrayAdapter(currentContext, android.R.layout.simple_list_item_1, availableCountries);

            listView = (ListView) view.findViewById(R.id.availbleCountriesListview);

            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    currentSelectedCountry = Country.valueOf(arrayAdapter.getItem(i).toString());
                    Toast.makeText(currentContext, currentSelectedCountry.getUrl(), Toast.LENGTH_SHORT).show();

                    bundleFromChooseCountryFragment.putString("country", currentSelectedCountry.name());
                    getParentFragmentManager().setFragmentResult("bundleFromChooseCountryFragment", bundleFromChooseCountryFragment);
                }
            });

            binding.buttonLogout.setOnClickListener(view1 -> NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment));

            binding.buttonNext.setOnClickListener(view12 -> {
                if (currentSelectedCountry != null) {
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FisheringMadeFragment);
                } else {
                    Toast.makeText(currentContext, "Please choose a Country first.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private Promise<Void> bundlePromiseMethod() {
        Promise<Void> promise = new Promise<>();

        getParentFragmentManager().setFragmentResultListener("bundleFromLoginFragment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                bundleFromChooseCountryFragment.putLong("memberId", result.getLong("memberId"));
                promise.fulfill(null);
            }
        });
        return promise;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}