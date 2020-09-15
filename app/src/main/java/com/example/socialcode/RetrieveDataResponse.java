package com.example.socialcode;

import com.google.gson.annotations.SerializedName;

public class RetrieveDataResponse {
    private Integer status;
    private String message;

    @SerializedName("body")
    private String text;

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
