package com.bawaaba.rninja4.rookie.dashboard.subcatogery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bawaaba.rninja4.rookie.R;

/**
 * Created by rninja4 on 7/16/18.
 */

public class FragmentSubcategory extends Fragment {



    View rootView;

    public FragmentSubcategory() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_subcatogery, container, false);
        return rootView;
    }


}
