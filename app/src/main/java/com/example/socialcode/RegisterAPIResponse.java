package com.example.socialcode;

import com.google.gson.annotations.SerializedName;

public class RegisterAPIResponse {
    private int status;
    private String message;

    @SerializedName("body")
    private String text;

    public String getText() {
        return text;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
