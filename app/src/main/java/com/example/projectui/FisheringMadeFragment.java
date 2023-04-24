package com.example.projectui;

import static com.example.projectui.service.RestApiCallServiceImpl.GET_FISHERINGMADE;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.databinding.FisheringmadeFragmentBinding;
import com.example.projectui.service.RestApiCallServiceImpl;

import org.json.simple.JSONObject;

import java.util.Objects;

public class FisheringMadeFragment  extends Fragment {

    private FisheringmadeFragmentBinding binding;

    private Long memberId;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

//        getParentFragmentManager().setFragmentResultListener("bundleFromChooseCountryFragment", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                Bundle bundleFromChooseCountryFragment = new Bundle();
//                bundleFromChooseCountryFragment.putString("country", result.getString("country"));
//                getParentFragmentManager().setFragmentResult("bundleFromFisheringMadeFragment", bundleFromChooseCountryFragment);
//            }
//        });

        binding = FisheringmadeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context currentContext = getActivity().getApplicationContext();

        getParentFragmentManager().setFragmentResultListener("bundleFromLoginFragment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                memberId = result.getLong("memberId");
            }
        });

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value", memberId);
        RestApiCallServiceImpl restApiCallService = new RestApiCallServiceImpl();

        JSONObject returnJsonObject = restApiCallService.sendGetRequest(GET_FISHERINGMADE, jsonObject);


        if (Objects.nonNull(returnJsonObject)) {

        } else {
            Toast.makeText(currentContext, "No ", Toast.LENGTH_SHORT).show();
        }

        binding.buttonRegisternewcatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FisheringMadeFragment.this)
                        .navigate(R.id.action_FisheringMadeFragment_to_CreateFisheringMadeFragment);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
