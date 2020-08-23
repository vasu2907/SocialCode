package com.example.socialcode;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class ContestAPIResponse {
    private Integer status;
    private String message;
    private List<HashMap<String, String>> contest_lists;

    @SerializedName("body")
    private String text;

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<HashMap<String, String>> getContest_lists() {
        return contest_lists;
    }
}
