package com.example.projectui;

import static com.example.projectui.Helper.RESTApiRequestURL.GET_TYPEOFFISHLIST;

import android.content.Context;
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
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectui.ListAdapter.TypeOfFishListAdapter;
import com.example.projectui.databinding.ManageTypeoffishBinding;
import com.example.projectui.dto.ParcelableDTO;
import com.example.projectui.dto.TypeOfFish;
import com.example.projectui.service.RestApiCallServiceImpl;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class ManageTypeOfFishFragment extends Fragment {

    private ManageTypeoffishBinding binding;
    private Snackbar mySnackbar;
    private ListView listView;

    Bundle bundleFromManageTypeOfFishFragment = new Bundle();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ManageTypeoffishBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context currentContext = getActivity().getApplicationContext();

        initTypeOfFishListView(currentContext, view);

        binding.buttonRegisterTypeOfFish.setOnClickListener(action -> {
            NavHostFragment.findNavController(ManageTypeOfFishFragment.this)
                    .navigate(R.id.action_ManageTypeOfFishFragment_to_RegisterTypeOfFishFragment);
        });

        binding.buttonBACK.setOnClickListener(action -> {
            NavHostFragment.findNavController(ManageTypeOfFishFragment.this)
                    .navigate(R.id.action_ManageTypeOfFishFragment_to_AdminPage);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initTypeOfFishListView(Context currentContext, View view) {
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

                            JSONObject tofObject = (JSONObject) tof;

                            TypeOfFish typeOfFish = new TypeOfFish();
                            typeOfFish.setTypeOfFishId(Long.valueOf(tofObject.get("typeOfFishId").toString()));
                            typeOfFish.setActive(Boolean.valueOf(tofObject.get("active").toString()));
                            typeOfFish.setTypeOfFishName(tofObject.get("typeOfFishName").toString());

                            byte[] decodedString = Base64.getDecoder().decode(tofObject.get("typeOfFishPictureBase64").toString());
                            typeOfFish.setTypeOfFishPictureBase64(decodedString);
                            typeOfFishList.add(typeOfFish);
                        });

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            TypeOfFishListAdapter typeOfFishListAdapter = new TypeOfFishListAdapter(currentContext, typeOfFishList);
            listView = (ListView) view.findViewById(R.id.typeOfFishListView);

            listView.setAdapter(typeOfFishListAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TypeOfFish typeOfFishSelected = typeOfFishList.get(i);
                    ParcelableDTO parcelableDTO = new ParcelableDTO(typeOfFishSelected);

                    bundleFromManageTypeOfFishFragment.putParcelable("parcelableDTO", parcelableDTO);
                    getParentFragmentManager().setFragmentResult("bundleFromManageTypeOfFishFragment", bundleFromManageTypeOfFishFragment);
                    NavHostFragment.findNavController(ManageTypeOfFishFragment.this)
                            .navigate(R.id.action_ManageTypeOfFishFragment_to_RegisterTypeOfFishFragment);
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
