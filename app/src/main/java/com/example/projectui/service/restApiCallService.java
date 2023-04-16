package com.example.projectui.service;

import org.json.simple.JSONObject;

public interface restApiCallService {

    JSONObject sendPostRequest(String url, JSONObject obj);

}
