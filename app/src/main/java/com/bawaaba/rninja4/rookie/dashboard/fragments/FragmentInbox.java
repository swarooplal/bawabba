package com.bawaaba.rninja4.rookie.dashboard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bawaaba.rninja4.rookie.R;

import butterknife.ButterKnife;

/**
 * Created by rninja4 on 7/16/18.
 */

public class FragmentInbox extends Fragment {


    public FragmentInbox()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();

    }

}

