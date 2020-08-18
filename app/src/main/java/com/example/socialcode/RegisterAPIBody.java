package com.example.socialcode;

public class RegisterAPIBody {
    private String name, email, college, codeforces, codechef, hackerrank;

    public RegisterAPIBody(String name, String email, String college, String codeforces, String codechef, String hackerrank) {
        this.name = name;
        this.email = email;
        this.college = college;
        this.codeforces = codeforces;
        this.codechef = codechef;
        this.hackerrank = hackerrank;
    }

    public String getName() {
        return name;
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
