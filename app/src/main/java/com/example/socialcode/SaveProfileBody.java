package com.example.socialcode;

public class SaveProfileBody {
    private String name, email, college, codechef, codeforces, hackerrank, base64;

    public SaveProfileBody(String name, String email, String college, String codechef, String codeforces, String hackerrank, String base64) {
        this.name = name;
        this.email = email;
        this.college = college;
        this.codechef = codechef;
        this.codeforces = codeforces;
        this.hackerrank = hackerrank;
        this.base64 = base64;

    }

    public String getBase64() {
        return base64;
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

    public String getCodechef() {
        return codechef;
    }

    public String getCodeforces() {
        return codeforces;
    }

    public String getHackerrank() {
        return hackerrank;
    }
}
