package com.example.socialcode;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

public class ProfileTab extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1, tab2;
    public PageAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tab);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tab1 = (TabItem) findViewById(R.id.tab1);
        tab2= (TabItem) findViewById(R.id.tab2);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0){
                    pagerAdapter.notifyDataSetChanged();
                }else if (tab.getPosition() == 1){
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
}
