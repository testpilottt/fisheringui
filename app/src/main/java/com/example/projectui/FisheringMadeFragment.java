package com.example.projectui;

import static com.example.projectui.Helper.RESTApiRequestURL.GET_FISHERINGMADE;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.ListAdapter.FisheringMadeListAdapter;
import com.example.projectui.databinding.FisheringmadeFragmentBinding;
import com.example.projectui.dto.FisheringMade;
import com.example.projectui.dto.TypeOfFish;
import com.example.projectui.enums.Country;
import com.example.projectui.enums.Region;
import com.example.projectui.service.RestApiCallServiceImpl;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.chromium.base.Promise;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class FisheringMadeFragment  extends Fragment {

    private FisheringmadeFragmentBinding binding;

    private Long memberId;

    Bundle bundleFromFisheringMadeFragment = new Bundle();

    private Snackbar mySnackbar;

    private ListView listView;

    private Long currentMemberId;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FisheringmadeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context currentContext = getActivity().getApplicationContext();

        bundlePromiseMethod().then(v1 -> {
            initFisheringMadeListView(currentContext, view);
        });

        binding.buttonRegisternewcatch.setOnClickListener(view12 -> NavHostFragment.findNavController(FisheringMadeFragment.this)
                .navigate(R.id.action_FisheringMadeFragment_to_CreateFisheringMadeFragment));

        binding.buttonLogout.setOnClickListener(view1 -> NavHostFragment.findNavController(FisheringMadeFragment.this)
                .navigate(R.id.action_FisheringMadeFragment_to_FirstFragment));

    }

    private Promise<Void> bundlePromiseMethod() {
        Promise<Void> promise = new Promise<>();

        getParentFragmentManager().setFragmentResultListener("bundleFromChooseCountryFragment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                currentMemberId = result.getLong("memberId");
                bundleFromFisheringMadeFragment.putLong("memberId", currentMemberId);
                bundleFromFisheringMadeFragment.putString("country", result.getString("country"));
                getParentFragmentManager().setFragmentResult("bundleFromFisheringMadeFragment", bundleFromFisheringMadeFragment);
                promise.fulfill(null);
            }
        });

        getParentFragmentManager().setFragmentResultListener("bundleFromLoginFragment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                currentMemberId = result.getLong("memberId");
                bundleFromFisheringMadeFragment.putLong("memberId", currentMemberId);
                bundleFromFisheringMadeFragment.putString("country", result.getString("country"));
                getParentFragmentManager().setFragmentResult("bundleFromFisheringMadeFragment", bundleFromFisheringMadeFragment);
                promise.fulfill(null);
            }
        });

        getParentFragmentManager().setFragmentResultListener("bundleFromCreateFisheringMadeFragment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                currentMemberId = result.getLong("memberId");
                bundleFromFisheringMadeFragment.putLong("memberId", currentMemberId);
                bundleFromFisheringMadeFragment.putString("country", result.getString("country"));
                getParentFragmentManager().setFragmentResult("bundleFromFisheringMadeFragment", bundleFromFisheringMadeFragment);
                promise.fulfill(null);
            }
        });

        return promise;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initFisheringMadeListView(Context currentContext, View view) {
        RestApiCallServiceImpl restApiCallService = new RestApiCallServiceImpl();
        List<FisheringMade> fisheringMadeList = new ArrayList<>();
        JSONObject jsonObjectInput = new JSONObject();
        jsonObjectInput.put("value", "/" + currentMemberId);
        mySnackbar = Snackbar.make(view, "", BaseTransientBottomBar.LENGTH_LONG);
        restApiCallService.sendGetRequest(GET_FISHERINGMADE, jsonObjectInput).then(returnJsonObject -> {
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

                            FisheringMade fisheringMade = new FisheringMade();
                            fisheringMade.setFisheringId(Long.valueOf(jsonObject.get("fisheringId").toString()));
                            fisheringMade.setWeightKg(Double.valueOf(jsonObject.get("weightKg").toString()));
                            fisheringMade.setRegion(Region.valueOf(jsonObject.get("region").toString()));
                            fisheringMade.setCountry(Country.valueOf(jsonObject.get("country").toString()));

                            fisheringMade.setTimeLog(LocalDateTime.parse(jsonObject.get("timeLog").toString()));
                            byte[] decodedString = Base64.getDecoder().decode(jsonObject.get("pictureOfFishBase64").toString());
                            fisheringMade.setPictureOfFishBase64(decodedString);
                            JSONObject typeOfFishJsonObject = (JSONObject) jsonObject.get("typeOfFish");

                            TypeOfFish typeOfFish = new TypeOfFish();

                            typeOfFish.setTypeOfFishName(typeOfFishJsonObject.get("typeOfFishName").toString());
                            decodedString = Base64.getDecoder().decode(typeOfFishJsonObject.get("typeOfFishPictureBase64").toString());
                            typeOfFish.setTypeOfFishPictureBase64(decodedString);
                            typeOfFish.setActive(Boolean.valueOf(typeOfFishJsonObject.get("active").toString()));
                            fisheringMade.setTypeOfFish(typeOfFish);
                            fisheringMadeList.add(fisheringMade);
                        });

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        FisheringMadeListAdapter fisheringMadeListAdapter = new FisheringMadeListAdapter(currentContext, fisheringMadeList);
        listView = (ListView) view.findViewById(R.id.fisheringMadeListView);

        listView.setAdapter(fisheringMadeListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FisheringMade typeOfFishSelected = fisheringMadeList.get(i);

                Intent intent = new Intent(currentContext, PopUpWindowImageView.class);
                intent.putExtra("typeOfFishPictureByte", typeOfFishSelected.getTypeOfFish().getTypeOfFishPictureBase64());
                startActivity(intent);
            }
        });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
