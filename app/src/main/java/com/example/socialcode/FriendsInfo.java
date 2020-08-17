package com.example.socialcode;

public class FriendsInfo {
    String name,email,profilepic,codeforces_handle;

    public FriendsInfo(String name, String email, String profilepic,String codeforces_handle) {
        this.name = name;
        this.email = email;
        this.profilepic = profilepic;
        this.codeforces_handle = codeforces_handle;
    }

    public String getCodeforces_handle() {
        return codeforces_handle;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilepic() {
        return profilepic;
    }
}
