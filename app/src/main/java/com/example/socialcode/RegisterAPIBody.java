package com.example.socialcode;

public class RegisterAPIBody {
    private String name, email, college, codeforces, codechef, hackerrank, password;

    public RegisterAPIBody(String name, String email, String password, String college, String codeforces, String codechef, String hackerrank) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.college = college;
        this.codeforces = codeforces;
        this.codechef = codechef;
        this.hackerrank = hackerrank;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCollege() {
        return college;
    }

    public String getCodeforces() {
        return codeforces;
    }

    public String getCodechef() {
        return codechef;
    }

    public String getHackerrank() {
        return hackerrank;
    }
}
