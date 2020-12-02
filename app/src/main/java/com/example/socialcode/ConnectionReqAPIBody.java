package com.example.socialcode;

public class ConnectionReqAPIBody {
    private String src, dest;

    public ConnectionReqAPIBody(String src, String dest) {
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
