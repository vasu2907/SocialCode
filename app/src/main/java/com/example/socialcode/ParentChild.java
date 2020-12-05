package com.example.socialcode;

import java.util.ArrayList;

public class ParentChild {

    String category;
    ArrayList<Child> child;

    public ParentChild() {
        this.category = "DP";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Child> getChild() {
        return child;
    }

    public void setChild(ArrayList<Child> child) {
        this.child = child;
    }
}
