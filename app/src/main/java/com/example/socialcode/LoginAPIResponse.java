package com.example.socialcode;

import com.google.gson.annotations.SerializedName;

public class LoginAPIResponse {
    private String codeforces_rating;
    private String codeforces_friends;
    private String codeforces_contests;
    private String name;
    private Boolean verified;
    private String message;
    private String college;
    private Integer status;
    private Boolean credentials;

    public String getCollege() {
        return college;
    }

    @SerializedName("body")
    private String text;

    public Boolean getCredentials() {
        return credentials;
    }

    public String getText() {
        return text;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCodeforces_rating() {
        return codeforces_rating;
    }

    public String getCodeforces_friends() {
        return codeforces_friends;
    }

    public String getCodeforces_contests() {
        return codeforces_contests;
    }

    public String getName() {
        return name;
    }

    public Boolean getVerified() {
        return verified;
    }
}
