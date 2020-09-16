package com.example.socialcode;

public class SearchFriendAPIBody {
    private String pattern;

    public SearchFriendAPIBody(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
