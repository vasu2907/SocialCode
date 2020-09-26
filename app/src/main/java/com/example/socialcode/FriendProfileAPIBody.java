package com.example.socialcode;

public class FriendProfileAPIBody {
    private String src, dest;

    public FriendProfileAPIBody(String src, String dest) {
        this.src = src;
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }
}
