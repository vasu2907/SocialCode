package com.example.socialcode;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchFriendAPIResponse {
    private Integer status;
    private String message;
    ArrayList<HashMap<String, String>> payload;

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<HashMap<String, String>> getPayload() {
        return payload;
    }
}
