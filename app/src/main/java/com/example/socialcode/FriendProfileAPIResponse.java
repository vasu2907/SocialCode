package com.example.socialcode;


import java.util.ArrayList;
import java.util.HashMap;

public class FriendProfileAPIResponse {
    private String message, name, email, codeforces_rating, codeforces_friends, codeforces_contest, base64;
    private Integer status;
    private Boolean friend;
    private HashMap<String, ArrayList<String>> problems;

    public HashMap<String, ArrayList<String>> getProblems() {
        return problems;
    }

    public String getBase64() {
        return base64;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCodeforces_rating() {
        return codeforces_rating;
    }

    public String getCodeforces_friends() {
        return codeforces_friends;
    }

    public String getCodeforces_contest() {
        return codeforces_contest;
    }

    public Integer getStatus() {
        return status;
    }

    public Boolean getFriend() {
        return friend;
    }
}
