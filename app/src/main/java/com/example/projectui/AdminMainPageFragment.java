package com.example.projectui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.databinding.AdminMainpageBinding;

public class AdminMainPageFragment  extends Fragment {

    private AdminMainpageBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = AdminMainpageBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context currentContext = getActivity().getApplicationContext();

        binding.buttonManageTypeOfFish.setOnClickListener(action -> {
            NavHostFragment.findNavController(AdminMainPageFragment.this)
                    .navigate(R.id.action_AdminMainPageFragment_to_ManageTypeOfFishFragment);
        });

        binding.buttonLogout.setOnClickListener(action -> {
            NavHostFragment.findNavController(AdminMainPageFragment.this)
                    .navigate(R.id.action_AdminMainPageFragment_to_FirstFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
