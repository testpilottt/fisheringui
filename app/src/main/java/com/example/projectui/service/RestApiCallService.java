package com.example.projectui.service;

import org.chromium.base.Promise;
import org.json.simple.JSONObject;

public interface RestApiCallService {

    Promise<JSONObject> sendPostRequest(String url, JSONObject jsonObject);

    Promise<JSONObject> sendGetRequest(String url, JSONObject jsonObject);

}
