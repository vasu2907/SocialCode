package com.example.socialcode;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class DynamoUserInfo {
    String email, college, name;
    Boolean verified;
    HashMap<String, String> codechef, codeforces, hackerrank;

    @SerializedName("body")
    private String text;

    public String getEmail() {
        return email;
    }

    public String getCollege() {
        return college;
    }

    public String getName() {
        return name;
    }

    public Boolean getVerified() {
        return verified;
    }

    public HashMap<String, String> getCodechef() {
        return codechef;
    }

    public HashMap<String, String> getCodeforces() {
        return codeforces;
    }

    public HashMap<String, String> getHackerrank() {
        return hackerrank;
    }
}
