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
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.databinding.FragmentSecondBinding;
import com.example.projectui.enums.Country;

import java.util.Arrays;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private ListView listView;
    private Country currentSelectedCountry;

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

        List<Country> availableCountries = Arrays.asList(Country.values());

        ArrayAdapter arrayAdapter = new ArrayAdapter(currentContext, android.R.layout.simple_list_item_1, availableCountries);

        listView = (ListView) view.findViewById(R.id.availbleCountriesListview);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentSelectedCountry = Country.valueOf(arrayAdapter.getItem(i).toString());
                Toast.makeText(currentContext, currentSelectedCountry.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSelectedCountry != null) {
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FisheringMadeFragment);
                } else {
                    Toast.makeText(currentContext, "Please choose a Country first.", Toast.LENGTH_SHORT).show();
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