package com.example.projectui.service;

import static com.example.projectui.MainActivity.JSON;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class RestApiCallServiceImpl implements RestApiCallService {
    @Override
    public JSONObject sendPostRequest(String url, JSONObject jsonObject) {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toJSONString());

        Request postRequest = new Request.Builder()
                .url(url)
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

    @Override
    public JSONObject sendGetRequest(String url, JSONObject jsonObject) {
        OkHttpClient client = new OkHttpClient();

        String param = Objects.nonNull(jsonObject) ? String.valueOf(jsonObject.get("value")) : "";

        Request getRequest = new Request.Builder()
                .url(url + param)
                .get()
                .build();

        try {
            Response response = client.newCall(getRequest).execute();

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
