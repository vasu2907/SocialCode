package com.example.socialcode;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2 extends Fragment {


    public Tab2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_tab2, container, false);

        // Pass the arguments
        // to the parentItemAdapter.
        // These arguments are passed
        // using a method ParentItemList()

        // Set the layout manager
        // and adapter for items
        // of the parent recyclerview

        return view;
    }

}
