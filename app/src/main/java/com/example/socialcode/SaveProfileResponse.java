package com.example.socialcode;

import com.google.gson.annotations.SerializedName;

public class SaveProfileResponse {
    private String message;
    private Integer status;

    @SerializedName("body")
    private String text;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }
}
