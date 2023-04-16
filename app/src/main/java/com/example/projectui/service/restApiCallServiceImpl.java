package com.example.projectui.service;

import static com.example.projectui.MainActivity.JSON;
import static com.example.projectui.MainActivity.POST_MEMBERLOGIN;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.simple.JSONObject;

import java.io.IOException;

public class restApiCallServiceImpl implements restApiCallService {

    @Override
    public JSONObject sendPostRequest(String url, JSONObject jsonObject) {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toJSONString());

        Request postRequest = new Request.Builder()
                .url(POST_MEMBERLOGIN)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(postRequest).execute();

            JSONObject returnJsonObject = new JSONObject();

            returnJsonObject.put("code", response.code());
            returnJsonObject.put("body", response.body().string());

            return returnJsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
