package com.example.projectui.service;

import org.json.simple.JSONObject;

public interface RestApiCallService {

    JSONObject sendPostRequest(String url, JSONObject jsonObject);

    JSONObject sendGetRequest(String url, JSONObject jsonObject);

}
