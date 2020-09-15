package com.example.socialcode;

public class VerifyUserBody {
    private String email;

    public VerifyUserBody(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
