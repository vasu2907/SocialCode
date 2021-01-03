package com.example.socialcode;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.Gson;

import org.json.JSONObject;

public class PageAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    public PageAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new Tab2();
                return fragment;
            case 1:
                fragment = new Tab1();
                return fragment;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
