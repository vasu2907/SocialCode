package com.example.socialcode;

import com.google.gson.Gson;

import org.json.JSONObject;

public class ProblemsAPIResponse {
    JSONObject response;

    public ProblemsAPIResponse(JSONObject response) {
        this.response = response;
    }

    public JSONObject getResponse() {
        return response;
    }

    public void setResponse(JSONObject response) {
        this.response = response;
    }

    public String getJSONString() {
        return this.response.toString();
    }
}
