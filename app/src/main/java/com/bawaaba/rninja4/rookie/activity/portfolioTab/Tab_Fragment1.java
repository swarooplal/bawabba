package com.bawaaba.rninja4.rookie.activity.portfolioTab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bawaaba.rninja4.rookie.R;

/**
 * Created by rninja4 on 9/26/17.
 */

public class Tab_Fragment1  extends Fragment {

    View rootView;

    LinearLayout audio_layout;
    EditText Audio_title;
    EditText Audio_edit;
    Button Audio_button;
    Button AddMore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView= inflater.inflate(R.layout.tab_fragment_1, container, false);

        audio_layout=(LinearLayout)rootView.findViewById(R.id.audio_layout);
        Audio_title=(EditText)rootView.findViewById(R.id.audio_title);
        Audio_edit=(EditText)rootView.findViewById(R.id.audio_edit);
        Audio_button=(Button)rootView.findViewById(R.id.audio_button);
        AddMore=(Button)rootView.findViewById(R.id.add_more);

        return rootView;
    }


}