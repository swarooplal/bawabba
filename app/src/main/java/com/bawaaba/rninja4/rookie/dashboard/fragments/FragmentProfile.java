package com.bawaaba.rninja4.rookie.dashboard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bawaaba.rninja4.rookie.R;

/**
 * Created by rninja4 on 7/16/18.
 */

public class FragmentProfile extends Fragment {

    View rootView;

    public FragmentProfile() {

    }

    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        return rootView;
    }

}