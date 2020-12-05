package com.example.socialcode;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    public PageAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Tab2();
            case 1:
                return new Tab1();
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
