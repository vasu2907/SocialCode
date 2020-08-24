package com.example.socialcode;

public class LoginAPIBody {
    private String email, password;

    public LoginAPIBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
